package ui;

import dao.RecipeDAO;
import model.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/** Ana GUI penceresi – It’s Dinner Time */
public class RecipeAppFrame extends JFrame {

    /* -------- DAO ve modeller -------- */
    private final RecipeDAO dao;
    private final DefaultListModel<Recipe> mBreakfast = new DefaultListModel<>();
    private final DefaultListModel<Recipe> mLunch     = new DefaultListModel<>();
    private final DefaultListModel<Recipe> mDinner    = new DefaultListModel<>();

    /* -------- Listeler (seçim temizlemek için alan değişken) -------- */
    private JList<Recipe> lstBreakfast, lstLunch, lstDinner;

    /* -------- Detay paneli -------- */
    private final JTextPane txtDetails = new JTextPane();

    /* -------- Renk paleti -------- */
    private static final Color BG_PANEL  = new Color(0xF8F8FA);
    private static final Color BG_BRKFST = new Color(0xFFFBCC);
    private static final Color BG_LUNCH  = new Color(0xD5F5E3);
    private static final Color BG_DINNER = new Color(0xE8EAF6);

    public RecipeAppFrame(RecipeDAO dao) throws Exception {
        super("It’s Dinner Time • Recipe Manager");
        this.dao = dao;
        initLookAndFeel();
        loadData();
        buildUI();
        addGlobalShortcuts();
    }

    /* ---------- L&F ---------- */
    private void initLookAndFeel() {
        try { UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel"); }
        catch (Exception ignored) {}
        UIManager.put("control", BG_PANEL);
    }

    /* ---------- Veri yükleme ---------- */
    private void loadData() throws Exception {
        mBreakfast.clear(); mLunch.clear(); mDinner.clear();
        for (Recipe r : dao.findAll()) addToModel(r);
    }
    private void addToModel(Recipe r) {
        switch (r.getMealType()) {
            case BREAKFAST -> mBreakfast.addElement(r);
            case LUNCH     -> mLunch.addElement(r);
            case DINNER    -> mDinner.addElement(r);
        }
    }

    /* ---------- GUI kurulumu ---------- */
    private void buildUI() {
        setLayout(new BorderLayout(6,6));
        add(toolbar(), BorderLayout.NORTH);
        add(splitPane(), BorderLayout.CENTER);

        getContentPane().setBackground(BG_PANEL);
        setMinimumSize(new Dimension(800, 550));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    /* ----- Toolbar ----- */
    private JToolBar toolbar() {
        JToolBar tb = new JToolBar();
        tb.setFloatable(false);
        tb.setBackground(new Color(0xEBEDF4));

        JButton btnAdd = makeBtn("➕  Add", "Add new recipe");
        btnAdd.addActionListener(e -> showAddDialog());

        JButton btnAbout = makeBtn("ℹ  About", null);
        btnAbout.addActionListener(e ->
            JOptionPane.showMessageDialog(this,
                "It’s Dinner Time\nEnhanced GUI Demo\n© 2025",
                "About", JOptionPane.PLAIN_MESSAGE));

        tb.add(btnAdd); tb.addSeparator(); tb.add(btnAbout);
        return tb;
    }
    private JButton makeBtn(String text, String tip) {
        JButton b = new JButton(text);
        b.setFocusPainted(false); if (tip != null) b.setToolTipText(tip);
        return b;
    }

    /* ----- Orta kısım: sekmeler + detay ----- */
    private JSplitPane splitPane() {
        txtDetails.setEditable(false);
        txtDetails.setContentType("text/html");
        txtDetails.setBorder(new EmptyBorder(12,14,12,14));
        txtDetails.setBackground(Color.white);

        JSplitPane sp = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT, tabPane(), shadowScroll(txtDetails));
        sp.setResizeWeight(0.45);
        sp.setBorder(null);
        return sp;
    }

    private JTabbedPane tabPane() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.add("Breakfast", listScroll(mBreakfast, BG_BRKFST, MealType.BREAKFAST));
        tabs.add("Lunch",     listScroll(mLunch,     BG_LUNCH,  MealType.LUNCH));
        tabs.add("Dinner",    listScroll(mDinner,    BG_DINNER, MealType.DINNER));

        /* Sekme değişince tüm seçimleri temizle */
        tabs.addChangeListener(e -> clearSelectionAndDetails());
        return tabs;
    }

    /* ----- Liste paneli, seçim/deselect & çift tık yönetimi ----- */
    private JScrollPane listScroll(DefaultListModel<Recipe> model, Color bg, MealType type) {
        JList<Recipe> list = new JList<>(model);
        list.setBackground(bg);
        list.setSelectionBackground(bg.darker());
        list.setSelectionForeground(Color.black);
        list.setBorder(new EmptyBorder(6,6,6,6));
        list.setCellRenderer((l,v,i,s,f) -> {
            JLabel lb = new JLabel(v.toString());
            lb.setOpaque(true);
            lb.setFont(lb.getFont().deriveFont(s?Font.BOLD:Font.PLAIN));
            lb.setBackground(s ? bg.darker() : bg);
            lb.setBorder(new EmptyBorder(3,8,3,6));
            return lb;
        });
        list.addListSelectionListener(listSelectionHandler());
        list.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount()==1 && list.getSelectedIndex() ==
                        list.locationToIndex(e.getPoint())) {
                    // Aynı öğeye veya boş alana tıklandı → temizle
                    list.clearSelection();
                    txtDetails.setText("");
                }
            }
        });

        // List referanslarını sakla (geri temizleme için)
        switch (type) {
            case BREAKFAST -> lstBreakfast = list;
            case LUNCH     -> lstLunch     = list;
            case DINNER    -> lstDinner    = list;
        }
        return shadowScroll(list);
    }

    /* ----- Detay gösterimi ----- */
    private ListSelectionListener listSelectionHandler() {
        return e -> {
            if (e.getValueIsAdjusting()) return;
            @SuppressWarnings("unchecked") JList<Recipe> src = (JList<Recipe>) e.getSource();
            showDetails(src.getSelectedValue());
        };
    }
    private void showDetails(Recipe r) {
        if (r == null) { txtDetails.setText(""); return; }
        StringBuilder li = new StringBuilder();
        for (String i : r.getIngredients()) li.append("<li>").append(i).append("</li>");

        txtDetails.setText("""
            <html><body style='font-family:sans-serif;'>
            <h1 style='margin:0;'>%s</h1>
            <h3 style='color:#606487;'>%s</h3>
            <h3>Ingredients</h3><ul>%s</ul>
            <h3>Instructions</h3><p>%s</p>
            </body></html>
            """.formatted(r.getTitle(), r.getMealType(), li, r.getInstructions()));
        txtDetails.setCaretPosition(0);
    }

    /* ----- Add dialog ----- */
    private void showAddDialog() {
        AddRecipeDialog dlg = new AddRecipeDialog(this);
        dlg.setVisible(true);
        if (!dlg.isConfirmed()) return;
        Recipe r = dlg.build();
        try { dao.save(r); addToModel(r); }
        catch (Exception ex) { ex.printStackTrace(); }
    }

    /* ----- ESC veya sekme değişimi ile temizleme ----- */
    private void clearSelectionAndDetails() {
        if (lstBreakfast != null) lstBreakfast.clearSelection();
        if (lstLunch     != null) lstLunch.clearSelection();
        if (lstDinner    != null) lstDinner.clearSelection();
        txtDetails.setText("");
    }
    private void addGlobalShortcuts() {
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke("ESCAPE"), "clearSel");
        getRootPane().getActionMap().put("clearSel",
            new AbstractAction() { @Override public void actionPerformed(
                    java.awt.event.ActionEvent e) { clearSelectionAndDetails(); }});
    }
    /*
     *  DAHİLİ DECORATOR (Swing kalıbı)
     *  JScrollPane: Herhangi bir bileşene kaydırma yeteneği kazandırır.
     *  Ayrıca özel bir gölge efekti eklenmiştir.
     */
     
    /* ----- Gölge efektli ScrollPane oluşturur ----- */
    private JScrollPane shadowScroll(Component c) {
        JScrollPane sp = new JScrollPane(c);
        sp.setBorder(new ShadowBorder());
        sp.getViewport().setBackground(c.getBackground());
        return sp;
    }
    private static class ShadowBorder extends EmptyBorder {
        ShadowBorder() { super(1,1,6,6); }
        @Override public void paintBorder(Component c, Graphics g,int x,int y,int w,int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setPaint(new Color(0,0,0,25));
            g2.fill(new RoundRectangle2D.Double(x+2,y+2,w-4,h-4,10,10));
            g2.dispose();
        }
    }
}
