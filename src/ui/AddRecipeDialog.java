package ui;

import model.*;
import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

/* YENİ TARİF EKLEME PENCERESİ (Swing)
 * Kullanıcıdan başlık, öğün tipi, malzemeler ve talimatlar alır. */
class AddRecipeDialog extends JDialog {

    private boolean confirmed = false;
    private JTextField txtTitle = new JTextField(20);
    private JTextArea  txtIngr  = new JTextArea(4, 20);
    private JTextArea  txtInstr = new JTextArea(4, 20);
    private JComboBox<MealType> cbMeal = new JComboBox<>(MealType.values());

    AddRecipeDialog(JFrame owner) {
        super(owner, "Add New Recipe", true);
        buildUI();
        pack();
        setLocationRelativeTo(owner);
    }

    private void buildUI() {
        setLayout(new BorderLayout(5,5));
        JPanel form = new JPanel(new GridLayout(0,2,5,5));

        form.add(new JLabel("Title:"));      form.add(txtTitle);
        form.add(new JLabel("Meal Type:"));  form.add(cbMeal);
        form.add(new JLabel("Ingredients (;):"));
        form.add(new JScrollPane(txtIngr));
        form.add(new JLabel("Instructions:"));
        form.add(new JScrollPane(txtInstr));

        add(form, BorderLayout.CENTER);

        JPanel btns = new JPanel();
        JButton ok  = new JButton("Save");
        JButton can = new JButton("Cancel");
        btns.add(ok); btns.add(can);
        add(btns, BorderLayout.SOUTH);

        ok.addActionListener(e -> { confirmed = true; dispose(); });
        can.addActionListener(e -> dispose());
    }

    boolean isConfirmed() { return confirmed; }
 // Kullanıcının girdiği verilere göre yeni bir Recipe nesnesi oluşturur
    Recipe build() {
        String title = txtTitle.getText().trim();
        List<String> ing = Arrays.asList(txtIngr.getText().split("\\s*;\\s*"));
        String instr = txtInstr.getText().trim();
        MealType mt  = (MealType) cbMeal.getSelectedItem();
        return new BasicRecipe(null, title, ing, instr, mt);
    }
}
