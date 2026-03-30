package petshop;

import petshop.ui.JanelaPrincipal;

import javax.swing.*;
public class        Main {
    public static void main(String[] args) {
        // Aparência nativa do sistema operacional
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Mantém o padrão caso falhe
        }

        // Inicia a interface na EDT
        SwingUtilities.invokeLater(() -> {
            JanelaPrincipal janela = new JanelaPrincipal();
            janela.setVisible(true);
        });
    }
}
