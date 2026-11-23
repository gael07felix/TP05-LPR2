/*
 * CBTLPR2 - Trabalho Prático 05
 * Dupla:
 *   Gael Felix - CB3038912
 *   João Gabrel - CB3038009
 */
package tp05;

import java.awt.BorderLayout;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 * Painel que implementa Runnable: conta de 0 até o máximo
 * e preenche a lista. O label "Final" só aparece ao terminar.
 */
public class Painel extends JPanel implements Runnable {

    private final DefaultListModel<String> modelo = new DefaultListModel<>();
    private final JList<String> lista = new JList<>(modelo);
    private final JLabel lblFim = new JLabel("Final", SwingConstants.CENTER);

    private int maximo;

    public Painel() {
        setLayout(new BorderLayout(4, 4));
        lista.setVisibleRowCount(18);
        add(new JScrollPane(lista), BorderLayout.CENTER);

        lblFim.setVisible(false);
        add(lblFim, BorderLayout.SOUTH);
    }

    public void setMaximo(int maximo) {
        this.maximo = maximo;
    }

    public void limpar() {
        modelo.clear();
        lblFim.setVisible(false);
    }

    @Override
    public void run() {
        for (int i = 0; i < maximo; i++) {
            final int valor = i;
            SwingUtilities.invokeLater(() -> {
                modelo.addElement(String.valueOf(valor));
                lista.ensureIndexIsVisible(modelo.getSize() - 1);
            });
            try {
                // Pequena pausa para a contagem ficar visível na interface
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
        SwingUtilities.invokeLater(() -> lblFim.setVisible(true));
    }
}
