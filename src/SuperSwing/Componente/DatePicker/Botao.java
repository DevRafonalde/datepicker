package SuperSwing.Componente.DatePicker;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;

public final class Botao extends JButton {

    public boolean isPaintBackground() {
        return paintBackground;
    }

    public void setPaintBackground(boolean paintBackground) {
        this.paintBackground = paintBackground;
    }

    private Evento evento;
    private boolean paintBackground = true;
    private Color cor;

    public Botao() {
        setBorder(null);
        setContentAreaFilled(false);
        setFocusable(false);
        setBackground(Color.WHITE);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                if (!getText().equals("") && getName() != null) {
                    if (getName().equals("dia") || getName().equals("ano")) {
                        evento.executar(me, Integer.valueOf(getText()));
                    } else {
                        evento.executar(me, Integer.valueOf(getName()));
                    }
                    setBackground(getCor());
                    setForeground(new Color(255, 255, 255));
                }
            }
        });
    }

    public Evento getEvent() {
        return evento;
    }

    public void setEvent(Evento evento) {
        this.evento = evento;
    }

    @Override
    public void paint(Graphics grphcs) {
        if (paintBackground) {
            int width = getWidth();
            int height = getHeight();
            int size = Math.min(width, height);
            int x = (width - size) / 2;
            int y = (height - size) / 2;
            Graphics2D g2 = (Graphics2D) grphcs;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillOval(x, y, size, size);
        }
        super.paint(grphcs);
    }

    public Color getCor() {
        return cor;
    }

    public void setCor(Color cor) {
        this.cor = cor;
    }
}
