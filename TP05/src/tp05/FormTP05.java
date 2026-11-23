/*
 * CBTLPR2 - Trabalho Prático 05
 * Dupla:
 *   Gael Felix - CB3038912
 *   João Gabrel - CB3038009
 */
package tp05;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * Formulário principal do Trabalho Prático 5.
 * Inicia duas Threads com prioridades definidas pelo usuário.
 */
public class FormTP05 extends JFrame {

    private final JTextField txtMaximo = new JTextField("1000", 8);
    private final JTextField txtPrioridade1 = new JTextField("1", 4);
    private final JTextField txtPrioridade2 = new JTextField("10", 4);

    private final Painel painel1 = new Painel();
    private final Painel painel2 = new Painel();

    private final JButton btnIniciar = new JButton("Iniciar");
    private final JButton btnLimpar = new JButton("Limpar");

    private boolean emExecucao;

    public FormTP05() {
        super("Trabalho Prático 5");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        montarInterface();
        configurarEventos();

        pack();
        setLocationRelativeTo(null);
    }

    private void montarInterface() {
        JPanel pnlTopo = new JPanel(new GridBagLayout());
        pnlTopo.setBorder(BorderFactory.createEmptyBorder(8, 8, 4, 8));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 4, 2, 4);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        pnlTopo.add(new JLabel("Máximo:"), gbc);
        gbc.gridx = 1;
        pnlTopo.add(txtMaximo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        pnlTopo.add(new JLabel("Prioridade 1:"), gbc);
        gbc.gridx = 1;
        pnlTopo.add(txtPrioridade1, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        pnlTopo.add(new JLabel("Prioridade 2:"), gbc);
        gbc.gridx = 1;
        pnlTopo.add(txtPrioridade2, gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridheight = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        pnlTopo.add(new JLabel("{ Número entre 1 e 10"), gbc);

        JPanel pnlBotaoIniciar = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlBotaoIniciar.add(btnIniciar);

        JPanel pnlListas = new JPanel(new java.awt.GridLayout(1, 2, 8, 0));
        pnlListas.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        painel1.setBorder(BorderFactory.createEtchedBorder());
        painel2.setBorder(BorderFactory.createEtchedBorder());
        pnlListas.add(painel1);
        pnlListas.add(painel2);

        JPanel pnlBotaoLimpar = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlBotaoLimpar.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        pnlBotaoLimpar.add(btnLimpar);

        JPanel pnlNorte = new JPanel(new BorderLayout());
        pnlNorte.add(pnlTopo, BorderLayout.CENTER);
        pnlNorte.add(pnlBotaoIniciar, BorderLayout.SOUTH);

        add(pnlNorte, BorderLayout.NORTH);
        add(pnlListas, BorderLayout.CENTER);
        add(pnlBotaoLimpar, BorderLayout.SOUTH);

        setSize(420, 480);
    }

    private void configurarEventos() {
        btnIniciar.addActionListener(e -> iniciar());
        btnLimpar.addActionListener(e -> limpar());
    }

    private void iniciar() {
        if (emExecucao) {
            JOptionPane.showMessageDialog(this,
                    "As threads ainda estão em execução.",
                    "Aguarde", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int maximo;
        int prioridade1;
        int prioridade2;

        try {
            maximo = Integer.parseInt(txtMaximo.getText().trim());
            prioridade1 = Integer.parseInt(txtPrioridade1.getText().trim());
            prioridade2 = Integer.parseInt(txtPrioridade2.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Informe números válidos para Máximo e Prioridades.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (maximo <= 0) {
            JOptionPane.showMessageDialog(this,
                    "O valor máximo deve ser maior que zero.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!prioridadeValida(prioridade1) || !prioridadeValida(prioridade2)) {
            JOptionPane.showMessageDialog(this,
                    "As prioridades devem ser números entre 1 e 10.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        painel1.limpar();
        painel2.limpar();
        painel1.setMaximo(maximo);
        painel2.setMaximo(maximo);

        Thread t1 = new Thread(painel1);
        Thread t2 = new Thread(painel2);
        t1.setPriority(prioridade1);
        t2.setPriority(prioridade2);

        emExecucao = true;
        btnIniciar.setEnabled(false);

        // Monitora o fim das duas threads para reabilitar o botão
        new Thread(() -> {
            try {
                t1.start();
                t2.start();
                t1.join();
                t2.join();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            } finally {
                SwingUtilities.invokeLater(() -> {
                    emExecucao = false;
                    btnIniciar.setEnabled(true);
                });
            }
        }, "monitor-threads").start();
    }

    private boolean prioridadeValida(int prioridade) {
        return prioridade >= Thread.MIN_PRIORITY && prioridade <= Thread.MAX_PRIORITY;
    }

    private void limpar() {
        if (emExecucao) {
            JOptionPane.showMessageDialog(this,
                    "Aguarde o término das threads antes de limpar.",
                    "Aguarde", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        txtMaximo.setText("1000");
        txtPrioridade1.setText("1");
        txtPrioridade2.setText("10");
        painel1.limpar();
        painel2.limpar();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FormTP05().setVisible(true));
    }
}
