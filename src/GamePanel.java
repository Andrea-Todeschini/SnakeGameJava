import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.*;
import java.awt.event.KeyEvent;
import javax.swing.*;
import java.util.Arrays;
import java.util.Random;
public class GamePanel extends JPanel implements ActionListener {
    static final int Larghezza_Schermo = 1300;
    static final int Altezza_Schermo = 800;
    static final int Grandezza_Unita = 50;
    static final int Unita_Gioco = (Larghezza_Schermo*Altezza_Schermo)/Grandezza_Unita;
    static final int delay_timer = 50;
    final int x[] = new int[Unita_Gioco];
    final int y[] = new int[Unita_Gioco];
    int pezzi_corpo = 3;
    int mele_mangiate;
    int coordinate_X_mela;
    int coordinate_Y_mela;
    char direzione = 'R';
    boolean movimento = false;
    Timer timer;
    Random random;

    boolean restart = false;

    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(Larghezza_Schermo,Altezza_Schermo));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new adattatoreKey());
        avvioGioco();
    }
    public void avvioGioco(){
        if(restart){
            restart = false;
            Arrays.fill(x, 0);
            Arrays.fill(y,0);
            pezzi_corpo = 3;
            mele_mangiate = 0;
            direzione = 'R';
        }
        nuovamela();
        movimento = true;
        timer = new Timer(delay_timer,this);
        timer.start();
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        disegno(g);
    }
    public void disegno(Graphics g) {
        if (movimento) {
            for (int i = 0; i < (Larghezza_Schermo / Grandezza_Unita); i++) {
                g.drawLine(i * Grandezza_Unita, 0, i * Grandezza_Unita, Altezza_Schermo);
                g.drawLine(0, i * Grandezza_Unita, Larghezza_Schermo, i * Grandezza_Unita);
            }
            g.setColor(Color.red);
            g.fillOval(coordinate_X_mela, coordinate_Y_mela, Grandezza_Unita, Grandezza_Unita);

            for (int i = 0; i < pezzi_corpo; i++) {
                if (i == 0) {
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], Grandezza_Unita, Grandezza_Unita);
                } else {
                    g.setColor(new Color(45, 180, 0));
                    g.fillRect(x[i], y[i], Grandezza_Unita, Grandezza_Unita);
                }
            }
            g.setColor(Color.red);
            g.setFont(new Font("Ink Free",Font.BOLD,35));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Punteggio:"+ mele_mangiate,(Larghezza_Schermo - metrics.stringWidth("Punteggio:"+ mele_mangiate))/2,g.getFont().getSize());

        }
        else {
            fineGioco(g);
        }
    }

    public void nuovamela(){
        coordinate_X_mela = random.nextInt((int)(Larghezza_Schermo/Grandezza_Unita))*Grandezza_Unita;
        coordinate_Y_mela = random.nextInt((int)(Altezza_Schermo/Grandezza_Unita))*Grandezza_Unita;

    }
    public void movimento(){
        for(int i = pezzi_corpo; i>0;i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch(direzione){
            case 'U':
                y[0] = y[0] - Grandezza_Unita;
                break;
            case 'D':
                y[0] = y[0] + Grandezza_Unita;
                break;
            case 'L':
                x[0] = x[0] - Grandezza_Unita;
                break;
            case 'R':
                x[0] = x[0] + Grandezza_Unita;
                break;
        }
    }
    public void controlloMela(){
        if((x[0] == coordinate_X_mela) && (y[0] == coordinate_Y_mela)){
            pezzi_corpo++;
            mele_mangiate++;
            nuovamela();
        }
    }
    public void controlloCollisioni(){
        for(int i = pezzi_corpo;i>0;i--){
            if((x[0] == x[i]) && (y[0] == y[i])){
                movimento = false;
            }
        }
        if( x[0] < 0){
             movimento = false;
        }
        if( x[0] > Larghezza_Schermo){
            movimento = false;
        }
        if( y[0] < 0){
            movimento = false;
        }
        if( y[0] > Altezza_Schermo){
            movimento = false;
        }
        if(!movimento){
            timer.stop();
        }
    }
    public void fineGioco(Graphics g){
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free",Font.BOLD,75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over",(Larghezza_Schermo - metrics.stringWidth("Game Over"))/2,Altezza_Schermo/2);
        addRestartButton();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(movimento){
            movimento();
            controlloMela();
            controlloCollisioni();
        }
        repaint();
    }

    public class adattatoreKey extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent tasto){
            switch (tasto.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if(direzione != 'R'){
                        direzione = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direzione != 'L'){
                        direzione = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direzione != 'D'){
                        direzione = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direzione != 'U'){
                        direzione = 'D';
                    }
                    break;
            }
        }
    }

    public void addRestartButton() {
        String buttonText = "New Game?";
        JButton restartButton = new JButton(buttonText);
        Font font = new Font("Helvetica", Font.BOLD, 30);
        restartButton.setFont(font);


        setLayout(null);
        restartButton.setBounds(Larghezza_Schermo/2,(Altezza_Schermo/2)+50,300,30);
        add(restartButton);

        restartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                restartButton.setVisible(false);
                restart = true;
                avvioGioco();
            }
        });


    }


}
