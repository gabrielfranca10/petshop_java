package petshop.ui;

import petshop.db.Conexao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

public class JanelaPrincipal extends JFrame {

    public JanelaPrincipal() {
        setTitle("Sistema PetShop");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(900, 620);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(800, 500));

        try {
            Conexao.getConexao();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,
                "Não foi possível conectar ao banco de dados.\n" + ex.getMessage(),
                "Erro de Conexão", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        JPanel cabecalho = new JPanel(new BorderLayout());
        cabecalho.setBackground(new Color(30, 90, 150));
        cabecalho.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        JLabel titulo = new JLabel("🐾  Sistema de Gerenciamento PetShop");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        titulo.setForeground(Color.WHITE);
        cabecalho.add(titulo, BorderLayout.WEST);


        JTabbedPane abas = new JTabbedPane();
        abas.setFont(new Font("SansSerif", Font.PLAIN, 13));

        abas.addTab("👤  Tutores", new PainelTutor());
        abas.addTab("🐶  Pets",    new PainelPet());


        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rodape.setBackground(new Color(240, 240, 240));
        rodape.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
        JLabel lblRodape = new JLabel("Banco: PostgreSQL  |  JDBC puro — sem ORM");
        lblRodape.setFont(new Font("SansSerif", Font.ITALIC, 11));
        lblRodape.setForeground(Color.GRAY);
        rodape.add(lblRodape);

        add(cabecalho, BorderLayout.NORTH);
        add(abas,      BorderLayout.CENTER);
        add(rodape,    BorderLayout.SOUTH);


        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Conexao.fechar();
                dispose();
                System.exit(0);
            }
        });
    }
}
