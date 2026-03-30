package petshop.ui;

import petshop.dao.PetDAO;
import petshop.dao.TutorDAO;
import petshop.model.Pet;
import petshop.model.Tutor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class PainelPet extends JPanel {

    private final PetDAO    petDao    = new PetDAO();
    private final TutorDAO  tutorDao  = new TutorDAO();


    private final JTextField txtCodPet   = new JTextField(6);
    private final JTextField txtNome     = new JTextField(20);
    private final JTextField txtEspecie  = new JTextField(15);
    private final JTextField txtRaca     = new JTextField(20);
    private final JTextField txtPeso     = new JTextField(8);
    private final JComboBox<Tutor> cbTutor = new JComboBox<>();


    private final DefaultTableModel modeloTabela;
    private final JTable tabela;

    public PainelPet() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Dados do Pet"));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(4, 4, 4, 4);
        g.anchor = GridBagConstraints.WEST;

        txtCodPet.setEditable(false);
        txtCodPet.setBackground(new Color(230, 230, 230));

        adicionarCampo(form, g, "Cód. Pet (auto):", txtCodPet,  0);
        adicionarCampo(form, g, "Nome:",             txtNome,   1);
        adicionarCampo(form, g, "Espécie:",          txtEspecie, 2);
        adicionarCampo(form, g, "Raça:",             txtRaca,   3);
        adicionarCampo(form, g, "Peso (kg):",        txtPeso,   4);

        g.gridx = 0; g.gridy = 5;
        form.add(new JLabel("Tutor:"), g);
        g.gridx = 1;
        form.add(cbTutor, g);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton btnInserir   = new JButton("Inserir");
        JButton btnAlterar   = new JButton("Alterar");
        JButton btnDeletar   = new JButton("Deletar");
        JButton btnLimpar    = new JButton("Limpar");
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
                new String[]{"Cód.", "Nome", "Espécie", "Raça", "Peso", "CPF Tutor"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabela = new JTable(modeloTabela);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.getTableHeader().setReorderingAllowed(false);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        btnInserir  .addActionListener(e -> inserir());
        btnAlterar  .addActionListener(e -> alterar());
        btnDeletar  .addActionListener(e -> deletar());
        btnLimpar   .addActionListener(e -> limparCampos());
        btnAtualizar.addActionListener(e -> { carregarCbTutor(); carregarTabela(); });

        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) preencherFormulario();
        });

        carregarCbTutor();
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
        if (!validarCampos()) return;
        try {
            petDao.inserir(montarPet());
            JOptionPane.showMessageDialog(this, "Pet inserido com sucesso!");
            limparCampos();
            carregarTabela();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao inserir: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void alterar() {
        if (txtCodPet.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Selecione um pet na tabela.");
            return;
        }
        try {
            petDao.alterar(montarPet());
            JOptionPane.showMessageDialog(this, "Pet alterado com sucesso!");
            limparCampos();
            carregarTabela();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao alterar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deletar() {
        if (txtCodPet.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Selecione um pet na tabela.");
            return;
        }
        int cod = Integer.parseInt(txtCodPet.getText().trim());
        int confirm = JOptionPane.showConfirmDialog(this,
                "Confirma exclusão do pet " + txtNome.getText() + "?\n" +
                        "ATENÇÃO: todos os agendamentos deste pet também serão removidos.",
                "Confirmar exclusão", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            petDao.deletar(cod);
            JOptionPane.showMessageDialog(this, "Pet removido com sucesso!");
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
            for (Pet p : petDao.listarTodos()) {
                modeloTabela.addRow(new Object[]{
                        p.getCodPet(), p.getNome(), p.getEspecie(),
                        p.getRaca(), p.getPeso(), p.getCpfTutor()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar pets: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarCbTutor() {
        cbTutor.removeAllItems();
        try {
            for (Tutor t : tutorDao.listarTodos()) {
                cbTutor.addItem(t);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar tutores: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void preencherFormulario() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) return;
        txtCodPet .setText(String.valueOf(modeloTabela.getValueAt(linha, 0)));
        txtNome   .setText((String) modeloTabela.getValueAt(linha, 1));
        txtEspecie.setText((String) modeloTabela.getValueAt(linha, 2));
        txtRaca   .setText((String) modeloTabela.getValueAt(linha, 3));
        txtPeso   .setText(String.valueOf(modeloTabela.getValueAt(linha, 4)));

        String cpfTutor = (String) modeloTabela.getValueAt(linha, 5);
        for (int i = 0; i < cbTutor.getItemCount(); i++) {
            if (cbTutor.getItemAt(i).getCpf().equals(cpfTutor)) {
                cbTutor.setSelectedIndex(i);
                break;
            }
        }
    }

    private Pet montarPet() {
        int cod = txtCodPet.getText().isBlank() ? 0 : Integer.parseInt(txtCodPet.getText().trim());
        Tutor tutorSelecionado = (Tutor) cbTutor.getSelectedItem();
        return new Pet(
                cod,
                txtNome.getText().trim(),
                txtEspecie.getText().trim(),
                txtRaca.getText().trim(),
                Double.parseDouble(txtPeso.getText().trim().replace(",", ".")),
                tutorSelecionado != null ? tutorSelecionado.getCpf() : ""
        );
    }

    private void limparCampos() {
        txtCodPet.setText("");
        txtNome.setText("");   txtEspecie.setText("");
        txtRaca.setText("");   txtPeso.setText("");
        cbTutor.setSelectedIndex(0);
        tabela.clearSelection();
    }

    private boolean validarCampos() {
        if (txtNome.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Nome é obrigatório.");
            return false;
        }
        if (txtEspecie.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Espécie é obrigatória.");
            return false;
        }
        try {
            double peso = Double.parseDouble(txtPeso.getText().trim().replace(",", "."));
            if (peso <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Peso deve ser um número positivo.");
            return false;
        }
        if (cbTutor.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Selecione um tutor.");
            return false;
        }
        return true;
    }
}