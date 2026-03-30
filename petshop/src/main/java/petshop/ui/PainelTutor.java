package petshop.ui;

import petshop.dao.TutorDAO;
import petshop.model.Tutor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class PainelTutor extends JPanel {

    private final TutorDAO dao = new TutorDAO();

    private final JTextField txtCpf    = new JTextField(14);
    private final JTextField txtNome   = new JTextField(25);
    private final JTextField txtRua    = new JTextField(25);
    private final JTextField txtNumero = new JTextField(6);
    private final JTextField txtBairro = new JTextField(20);
    private final JTextField txtCep    = new JTextField(9);

    private final DefaultTableModel modeloTabela;
    private final JTable tabela;

    public PainelTutor() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Dados do Tutor"));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(4, 4, 4, 4);
        g.anchor = GridBagConstraints.WEST;

        adicionarCampo(form, g, "CPF:",    txtCpf,    0);
        adicionarCampo(form, g, "Nome:",   txtNome,   1);
        adicionarCampo(form, g, "Rua:",    txtRua,    2);
        adicionarCampo(form, g, "Número:", txtNumero, 3);
        adicionarCampo(form, g, "Bairro:", txtBairro, 4);
        adicionarCampo(form, g, "CEP:",    txtCep,    5);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton btnInserir  = new JButton("Inserir");
        JButton btnAlterar  = new JButton("Alterar");
        JButton btnDeletar  = new JButton("Deletar");
        JButton btnLimpar   = new JButton("Limpar");
        JButton btnAtualizar = new JButton("Atualizar Lista");

        btnInserir.setBackground(new Color(70, 130, 180));
        btnInserir.setForeground(Color.WHITE);
        btnInserir.setOpaque(true);
        btnInserir.setBorderPainted(false);
        btnAlterar.setBackground(new Color(255, 165, 0));
        btnAlterar.setForeground(Color.BLACK);
        btnAlterar.setOpaque(true);
        btnAlterar.setBorderPainted(false);
        btnDeletar.setBackground(new Color(200, 60, 60));
        btnDeletar.setForeground(Color.WHITE);
        btnDeletar.setOpaque(true);
        btnDeletar.setBorderPainted(false);

        botoes.add(btnInserir);
        botoes.add(btnAlterar);
        botoes.add(btnDeletar);
        botoes.add(btnLimpar);
        botoes.add(btnAtualizar);

        JPanel topo = new JPanel(new BorderLayout());
        topo.add(form,   BorderLayout.CENTER);
        topo.add(botoes, BorderLayout.SOUTH);
        add(topo, BorderLayout.NORTH);

        modeloTabela = new DefaultTableModel(
                new String[]{"CPF", "Nome", "Rua", "Número", "Bairro", "CEP"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabela = new JTable(modeloTabela);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.getTableHeader().setReorderingAllowed(false);
        add(new JScrollPane(tabela), BorderLayout.CENTER);
        btnInserir.addActionListener(e -> inserir());
        btnAlterar.addActionListener(e -> alterar());
        btnDeletar.addActionListener(e -> deletar());
        btnLimpar .addActionListener(e -> limparCampos());
        btnAtualizar.addActionListener(e -> carregarTabela());

        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) preencherFormulario();
        });

        carregarTabela();
    }

    private void adicionarCampo(JPanel p, GridBagConstraints g,
                                String label, JTextField campo, int linha) {
        g.gridx = 0; g.gridy = linha;
        p.add(new JLabel(label), g);
        g.gridx = 1;
        p.add(campo, g);
    }

    private void inserir() {
        if (!validarCamposObrigatorios()) return;
        try {
            dao.inserir(montarTutor());
            JOptionPane.showMessageDialog(this, "Tutor inserido com sucesso!");
            limparCampos();
            carregarTabela();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao inserir: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void alterar() {
        if (txtCpf.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Selecione um tutor na tabela.");
            return;
        }
        try {
            dao.alterar(montarTutor());
            JOptionPane.showMessageDialog(this, "Tutor alterado com sucesso!");
            limparCampos();
            carregarTabela();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao alterar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deletar() {
        String cpf = txtCpf.getText().trim();
        if (cpf.isBlank()) {
            JOptionPane.showMessageDialog(this, "Selecione um tutor na tabela.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "Confirma exclusão do tutor " + cpf + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            dao.deletar(cpf);
            JOptionPane.showMessageDialog(this, "Tutor removido com sucesso!");
            limparCampos();
            carregarTabela();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao deletar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarTabela() {
        modeloTabela.setRowCount(0);
        try {
            List<Tutor> lista = dao.listarTodos();
            for (Tutor t : lista) {
                modeloTabela.addRow(new Object[]{
                        t.getCpf(), t.getNome(), t.getRua(),
                        t.getNumero(), t.getBairro(), t.getCep()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar tutores: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void preencherFormulario() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) return;
        txtCpf   .setText((String) modeloTabela.getValueAt(linha, 0));
        txtNome  .setText((String) modeloTabela.getValueAt(linha, 1));
        txtRua   .setText((String) modeloTabela.getValueAt(linha, 2));
        txtNumero.setText((String) modeloTabela.getValueAt(linha, 3));
        txtBairro.setText((String) modeloTabela.getValueAt(linha, 4));
        txtCep   .setText((String) modeloTabela.getValueAt(linha, 5));
        txtCpf.setEditable(false);
    }

    private Tutor montarTutor() {
        return new Tutor(
                txtCpf.getText().trim(),
                txtNome.getText().trim(),
                txtRua.getText().trim(),
                txtNumero.getText().trim(),
                txtBairro.getText().trim(),
                txtCep.getText().trim()
        );
    }

    private void limparCampos() {
        txtCpf.setText(""); txtCpf.setEditable(true);
        txtNome.setText("");   txtRua.setText("");
        txtNumero.setText(""); txtBairro.setText("");
        txtCep.setText("");
        tabela.clearSelection();
    }

    private boolean validarCamposObrigatorios() {
        if (txtCpf.getText().trim().length() != 11) {
            JOptionPane.showMessageDialog(this, "CPF deve ter 11 dígitos.");
            return false;
        }
        if (txtNome.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Nome é obrigatório.");
            return false;
        }
        return true;
    }
}