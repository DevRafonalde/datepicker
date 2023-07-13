package SuperSwing.Componente.DatePicker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rafael.albuquerque
 */
public class DateChooser extends javax.swing.JPanel {

    LocalDate dataSelecionadaLocalDate;
    
    public JTextField getCampoTexto() {
        return campoTexto;
    }

    public void addEventoSelecaoData(EventoSelecaoData event) {
        eventos.add(event);
    }

    private JTextField campoTexto;
    private final String[] MESES = {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};
    private String formatoData = "dd-MM-yyyy";
    private int MES = 1;
    private int ANO = 2021;
    private int DIA = 1;
    private int STATUS = 1;   //  1 is dia    2 is mes  3 is ano
    private int anoInicial;
    private DataSelecionada dataSelecionada = new DataSelecionada();
    private List<EventoSelecaoData> eventos;

    /**
     * Creates new form DateChooser
     */
    public DateChooser() {
        initComponents();
        execute();
    }
    
    private void execute() {
        setForeground(new Color(41,76,113));
        eventos = new ArrayList<>();
        jPopupMenu_popup.add(DateChooser.this);
        setHoje(false);
    }

    public void setCampoTexto(JTextField txt) {
        this.campoTexto = txt;
        this.campoTexto.setEditable(false);
        this.campoTexto.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                if (campoTexto.isEnabled()) {
                    showPopup();
                }
            }
        });
        setText(false, 0);
    }

    public void setText(boolean runEvent, int act) {
        if (campoTexto != null) {
            try {
                LocalDate date = LocalDate.of(ANO, MES, DIA);
                campoTexto.setText(new SimpleDateFormat(formatoData).format(date));
            } catch (DateTimeException e) {
                System.err.println(e);
            }
        }
        if (runEvent) {
            runEvent(act);
        }
    }

    private void runEvent(int act) {
        AcaoSelecao action = new AcaoSelecao() {
            @Override
            public int getAcao() {
                return act;
            }
        };
        for (EventoSelecaoData event : eventos) {
            event.dataSelecao(action, dataSelecionada);
        }
    }

    private Evento getEventDay(Datas dates) {
        return (MouseEvent evt, int num) -> {
            dates.limparSelecao();
            dates.setDiaSelecionado(num);
            DIA = num;
            dataSelecionada.setDia(DIA);
            dataSelecionada.setMes(MES);
            dataSelecionada.setAno(ANO);
            setText(true, 1);
            if (evt != null && evt.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(evt)) {
                jPopupMenu_popup.setVisible(false);
            }
        };
    }

    private Evento getEventMonth() {
        return (MouseEvent evt, int num) -> {
            MES = num;
            dataSelecionada.setDia(DIA);
            dataSelecionada.setMes(MES);
            dataSelecionada.setAno(ANO);
            setText(true, 2);
            Datas d = new Datas();
            d.setForeground(getForeground());
            d.setEvent(getEventDay(d));
            d.mostrarData(MES, ANO, dataSelecionada);
            if (slider.slideToDown(d)) {
                cmdMes.setText(MESES[MES - 1]);
                cmdAno.setText(String.valueOf(ANO));
                STATUS = 1;
            }
        };
    }

    private Evento getEventYear() {
        return (MouseEvent evt, int num) -> {
            ANO = num;
            dataSelecionada.setDia(DIA);
            dataSelecionada.setMes(MES);
            dataSelecionada.setAno(ANO);
            setText(true, 3);
            Meses d = new Meses();
            d.setEvento(getEventMonth());
            if (slider.slideToDown(d)) {
                cmdMes.setText(MESES[MES - 1]);
                cmdAno.setText(String.valueOf(ANO));
                STATUS = 2;
            }
        };
    }

    private void setHoje(boolean runEvent) {
        LocalDate date = LocalDate.now();
        this.dataSelecionadaLocalDate = date;
        Datas dates = new Datas();
        dates.setForeground(getForeground());
        dates.setEvent(getEventDay(dates));
        DIA = date.getDayOfMonth();
        MES = date.getMonthValue();
        ANO = date.getYear();
        dataSelecionada.setDia(DIA);
        dataSelecionada.setMes(MES);
        dataSelecionada.setAno(ANO);
        dates.mostrarData(MES, ANO, dataSelecionada);
        slider.slideNon(dates);
        cmdMes.setText(MESES[MES - 1]);
        cmdAno.setText(String.valueOf(ANO));
        setText(runEvent, 0);
    }

    /**
     * Seta o dia de hoje como base
     */
    public void setHoje() {
        setHoje(true);
    }

    private void setDiaManual(boolean runEvent, LocalDate date) {
        this.dataSelecionadaLocalDate = date;
        Datas dates = new Datas();
        dates.setForeground(getForeground());
        dates.setEvent(getEventDay(dates));
        DIA = date.getDayOfMonth();
        MES = date.getMonthValue();
        ANO = date.getYear();
        dataSelecionada.setDia(DIA);
        dataSelecionada.setMes(MES);
        dataSelecionada.setAno(ANO);
        dates.mostrarData(MES, ANO, dataSelecionada);
        slider.slideNon(dates);
        cmdMes.setText(MESES[MES - 1]);
        cmdAno.setText(String.valueOf(ANO));
        setText(runEvent, 0);
    }

    /**
     * Metodo utilizado para setar uma data via código no DateChooser enviando um LocalDate como parâmetro
     * @param date Um LocalDate contendo a data selecionada
     */
    public void setDiaManual(LocalDate date) {
        setDiaManual(true, date);
    }

    /**
     * Metodo utilizado para setar uma data via código no DateChooser enviando os valores de dia, mês e ano como parâmetro
     * @param dia O valor do dia selecionado em int
     * @param mes O valor do mês selecionado em int
     * @param ano O valor do ano selecionado em int
     */
    public void setDiaManual(int dia, int mes, int ano) {
        setDiaManual(true, LocalDate.of(ano, mes, dia));
    }

    private void setDateNext() {
        Datas dates = new Datas();
        dates.setForeground(getForeground());
        dates.setEvent(getEventDay(dates));
        dates.mostrarData(MES, ANO, dataSelecionada);
        if (slider.slideToLeft(dates)) {
            cmdMes.setText(MESES[MES - 1]);
            cmdAno.setText(String.valueOf(ANO));
        }
    }

    private void setDateBack() {
        Datas dates = new Datas();
        dates.setForeground(getForeground());
        dates.setEvent(getEventDay(dates));
        dates.mostrarData(MES, ANO, dataSelecionada);
        if (slider.slideToRight(dates)) {
            cmdMes.setText(MESES[MES - 1]);
            cmdAno.setText(String.valueOf(ANO));
        }
    }

    private void setYearNext() {
        Anos anos = new Anos();
        anos.setEvent(getEventYear());
        anoInicial = anos.proximo(anoInicial);
        slider.slideToLeft(anos);
    }

    private void setYearBack() {
        if (anoInicial >= 1000) {
            Anos anos = new Anos();
            anos.setEvent(getEventYear());
            anoInicial = anos.anterior(anoInicial);
            slider.slideToLeft(anos);
        }
    }

    public void showPopup(Component com, int x, int y) {
        jPopupMenu_popup.show(com, x, y);
    }

    public void showPopup() {
        jPopupMenu_popup.show(campoTexto, 0, campoTexto.getHeight());
    }

    public void hidePopup() {
        jPopupMenu_popup.setVisible(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu_popup = new javax.swing.JPopupMenu(){
            @Override
            protected void paintComponent(Graphics grphcs) {
                grphcs.setColor(new Color(114, 113, 113));
                grphcs.fillRect(0, 0, getWidth(), getHeight());
                grphcs.setColor(Color.WHITE);
                grphcs.fillRect(1, 1, getWidth() - 2, getHeight() - 2);
            }
        };
        header = new javax.swing.JPanel();
        cmdAnterior = new SuperSwing.Componente.DatePicker.Botao();
        jLayeredPane_mesAno = new javax.swing.JLayeredPane();
        cmdMes = new SuperSwing.Componente.DatePicker.Botao();
        jLabel1 = new javax.swing.JLabel();
        cmdAno = new SuperSwing.Componente.DatePicker.Botao();
        cmdPosterior = new SuperSwing.Componente.DatePicker.Botao();
        slider = new SuperSwing.Componente.DatePicker.Slider();

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(262, 206));

        header.setBackground(new java.awt.Color(41, 76, 113));
        header.setMaximumSize(new java.awt.Dimension(262, 40));
        header.setPreferredSize(new java.awt.Dimension(262, 32));

        cmdAnterior.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Fontes/previous.png"))); // NOI18N
        cmdAnterior.setFocusable(true);
        cmdAnterior.setPaintBackground(false);
        cmdAnterior.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdAnteriorActionPerformed(evt);
            }
        });
        cmdAnterior.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmdAnteriorKeyPressed(evt);
            }
        });

        jLayeredPane_mesAno.setMinimumSize(new java.awt.Dimension(107, 21));
        jLayeredPane_mesAno.setPreferredSize(new java.awt.Dimension(107, 19));
        jLayeredPane_mesAno.setLayout(new java.awt.FlowLayout());

        cmdMes.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cmdMes.setForeground(new java.awt.Color(255, 255, 255));
        cmdMes.setText("Janeiro");
        cmdMes.setFocusPainted(false);
        cmdMes.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        cmdMes.setMaximumSize(new java.awt.Dimension(50, 19));
        cmdMes.setMinimumSize(new java.awt.Dimension(50, 19));
        cmdMes.setPaintBackground(false);
        cmdMes.setPreferredSize(new java.awt.Dimension(50, 19));
        cmdMes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdMesActionPerformed(evt);
            }
        });
        jLayeredPane_mesAno.add(cmdMes);

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("-");
        jLayeredPane_mesAno.add(jLabel1);

        cmdAno.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cmdAno.setForeground(new java.awt.Color(255, 255, 255));
        cmdAno.setText("2023");
        cmdAno.setFocusPainted(false);
        cmdAno.setFont(new java.awt.Font("Trebuchet MS", 0, 14)); // NOI18N
        cmdAno.setMaximumSize(new java.awt.Dimension(32, 19));
        cmdAno.setMinimumSize(new java.awt.Dimension(32, 19));
        cmdAno.setPaintBackground(false);
        cmdAno.setPreferredSize(new java.awt.Dimension(32, 19));
        cmdAno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdAnoActionPerformed(evt);
            }
        });
        jLayeredPane_mesAno.add(cmdAno);

        cmdPosterior.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Fontes/forward.png"))); // NOI18N
        cmdPosterior.setFocusable(true);
        cmdPosterior.setPaintBackground(false);
        cmdPosterior.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdPosteriorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout headerLayout = new javax.swing.GroupLayout(header);
        header.setLayout(headerLayout);
        headerLayout.setHorizontalGroup(
            headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cmdAnterior, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLayeredPane_mesAno, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmdPosterior, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        headerLayout.setVerticalGroup(
            headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmdPosterior, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmdAnterior, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLayeredPane_mesAno, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(header, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(slider, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(header, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(slider, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cmdMesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdMesActionPerformed
        if (STATUS != 2) {
            STATUS = 2;
            Meses mess = new Meses();
            mess.setEvento(getEventMonth());
            slider.slideToDown(mess);
        } else {
            Datas dates = new Datas();
            dates.setForeground(getForeground());
            dates.setEvent(getEventDay(dates));
            dates.mostrarData(MES, ANO, dataSelecionada);
            slider.slideToDown(dates);
            STATUS = 1;
        }
    }//GEN-LAST:event_cmdMesActionPerformed

    private void cmdAnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdAnoActionPerformed
        if (STATUS != 3) {
            STATUS = 3;
            Anos anos = new Anos();
            anos.setEvent(getEventYear());
            anoInicial = anos.mostrarAno(ANO);
            slider.slideToDown(anos);
        } else {
            Datas dates = new Datas();
            dates.setForeground(getForeground());
            dates.setEvent(getEventDay(dates));
            dates.mostrarData(MES, ANO, dataSelecionada);
            slider.slideToDown(dates);
            STATUS = 1;
        }
    }//GEN-LAST:event_cmdAnoActionPerformed

    private void cmdAnteriorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdAnteriorActionPerformed
        switch (STATUS) {
            case 1:
                //  Date
                if (MES == 1) {
                    MES = 12;
                    ANO--;
                } else {
                    MES--;
                }   setDateBack();
                break;
            case 3:
                //  Year
                setYearBack();
                break;
            default:
                if (ANO >= 1000) {
                    ANO--;
                    Meses mess = new Meses();
                    mess.setEvento(getEventMonth());
                    slider.slideToLeft(mess);
                    cmdAno.setText(String.valueOf(ANO));
                }   break;
        }
    }//GEN-LAST:event_cmdAnteriorActionPerformed

    private void cmdPosteriorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPosteriorActionPerformed
        switch (STATUS) {
            case 1:
                //  Date
                if (MES == 12) {
                    MES = 1;
                    ANO++;
                } else {
                    MES++;
                }   setDateNext();
                break;
            case 3:
                //  Year
                setYearNext();
                break;
            default:
                ANO++;
                Meses mess = new Meses();
                mess.setEvento(getEventMonth());
                slider.slideToLeft(mess);
                cmdAno.setText(String.valueOf(ANO));
                break;
        }
    }//GEN-LAST:event_cmdPosteriorActionPerformed

    private void cmdAnteriorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmdAnteriorKeyPressed
        switch (evt.getKeyCode()) {
            case KeyEvent.VK_UP:
                {
                    Component com = slider.getComponent(0);
                    if (com instanceof Datas) {
                        Datas d = (Datas) com;
                        d.cima();
                    }       break;
                }
            case KeyEvent.VK_DOWN:
                {
                    Component com = slider.getComponent(0);
                    if (com instanceof Datas) {
                        Datas d = (Datas) com;
                        d.baixo();
                    }       break;
                }
            case KeyEvent.VK_LEFT:
                {
                    Component com = slider.getComponent(0);
                    if (com instanceof Datas) {
                        Datas d = (Datas) com;
                        d.anterior();
                    }       break;
                }
            case KeyEvent.VK_RIGHT:
                {
                    Component com = slider.getComponent(0);
                    if (com instanceof Datas) {
                        Datas d = (Datas) com;
                        d.proximo();
                    }       break;
                }
            default:
                break;
        }
    }//GEN-LAST:event_cmdAnteriorKeyPressed

    public String getFormatoData() {
        return formatoData;
    }

    public void setFormatoData(String formatoData) {
        this.formatoData = formatoData;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private SuperSwing.Componente.DatePicker.Botao cmdAno;
    private SuperSwing.Componente.DatePicker.Botao cmdAnterior;
    private SuperSwing.Componente.DatePicker.Botao cmdMes;
    private SuperSwing.Componente.DatePicker.Botao cmdPosterior;
    private javax.swing.JPanel header;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLayeredPane jLayeredPane_mesAno;
    private javax.swing.JPopupMenu jPopupMenu_popup;
    private SuperSwing.Componente.DatePicker.Slider slider;
    // End of variables declaration//GEN-END:variables

    /**
     * Retorna a data que está atualmente selecionada no formato de um LocalDate
     * @return LocalDate dataSelecionada
     */
    public LocalDate getDataSelecionadaLocalDate() {
        return dataSelecionadaLocalDate;
    }

    @Override
    public void setForeground(Color color) {
        super.setForeground(color);
        if (header != null) {
            header.setBackground(color);
            setHoje(false);
        }
    }
}

