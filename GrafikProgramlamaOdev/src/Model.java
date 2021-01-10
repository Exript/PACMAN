
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Model extends JPanel implements ActionListener {

    private Dimension boyut;
    private final Font yaziBoyutu = new Font("Arial", Font.BOLD, 14);
    private boolean oyunDevam = false;
    private boolean olumDurumu = false; //Olum durumy

    private final int blok_boyutu = 24;
    private final int n_blok = 15;
    private final int ekran_buyuklugu = n_blok * blok_boyutu;
    private final int hayalet_sayisi = 12;
    private final int pacman_hiz = 6;

    private int n_hayalet = 6;
    private int kalan_can, skor;
    private int[] dx, dy;
    private int[] hayalet_x, hayalet_y, hayalet_dx, hayalet_dy, hayaletHiz;

    private Image kalp, hayalet;
    private Image yukari, asagi, sol, sag;

    private int pacman_x, pacman_y, pacmand_x, pacmand_y;
    private int req_dx, req_dy;

    private final short seviyeBilgileri[] = {
            19, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 22,
            17, 16, 16, 16, 16, 24, 16, 16, 16, 16, 16, 16, 16, 16, 20,
            25, 24, 24, 24, 28, 0, 17, 16, 16, 16, 16, 16, 16, 16, 20,
            0, 0, 0, 0, 0, 0, 17, 16, 16, 16, 16, 16, 16, 16, 20,
            19, 18, 18, 18, 18, 18, 16, 16, 16, 16, 24, 24, 24, 24, 20,
            17, 16, 16, 16, 16, 16, 16, 16, 16, 20, 0, 0, 0, 0, 21,
            17, 16, 16, 16, 16, 16, 16, 16, 16, 20, 0, 0, 0, 0, 21,
            17, 16, 16, 16, 24, 16, 16, 16, 16, 20, 0, 0, 0, 0, 21,
            17, 16, 16, 20, 0, 17, 16, 16, 16, 16, 18, 18, 18, 18, 20,
            17, 24, 24, 28, 0, 25, 24, 24, 16, 16, 16, 16, 16, 16, 20,
            21, 0, 0, 0, 0, 0, 0, 0, 17, 16, 16, 16, 16, 16, 20,
            17, 18, 18, 22, 0, 19, 18, 18, 16, 16, 16, 16, 16, 16, 20,
            17, 16, 16, 20, 0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 20,
            17, 16, 16, 20, 0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 20,
            25, 24, 24, 24, 26, 24, 24, 24, 24, 24, 24, 24, 24, 24, 28
    };

    private final int gecerli_hizlar[] = {1, 2, 3, 4, 6, 8};
    private final int maksHiz = 6;

    private int hareketHizi = 3;
    private short[] ekranBilgileri;
    private Timer timer;

    public Model() {

        goruntuleri_yukle();
        baslangic_degiskenler();
        addKeyListener(new TAdapter());
        setFocusable(true);
        oyun_sifirla();
    }


    private void goruntuleri_yukle() {
        asagi = new ImageIcon("src/goruntuler/asagi.gif").getImage();
        yukari = new ImageIcon("src/goruntuler/yukari.gif").getImage();
        sol = new ImageIcon("src/goruntuler/sol.gif").getImage();
        sag = new ImageIcon("src/goruntuler/sag.gif").getImage();
        hayalet = new ImageIcon("src/goruntuler/hayalet.gif").getImage();
        kalp = new ImageIcon("src/goruntuler/kalp.png").getImage();

    }

    private void baslangic_degiskenler() {

        ekranBilgileri = new short[n_blok * n_blok];
        boyut = new Dimension(400, 400);
        hayalet_x = new int[hayalet_sayisi];
        hayalet_dx = new int[hayalet_sayisi];
        hayalet_y = new int[hayalet_sayisi];
        hayalet_dy = new int[hayalet_sayisi];
        hayaletHiz = new int[hayalet_sayisi];
        dx = new int[4];
        dy = new int[4];

        timer = new Timer(40, this);
        timer.start();
    }

    private void oyun_oyna(Graphics2D g2d) {

        if (olumDurumu) {

            death();

        } else {

            pacman_hareket();
            pacman_ciz(g2d);
            hayalet_hareket(g2d);
            labirent_kontrol();
        }
    }

    private void girisEkraniGoster(Graphics2D g2d) {

        String start = "Baslamak icin SPACE tusuna basin";
        g2d.setColor(Color.PINK);
        g2d.drawString(start, (ekran_buyuklugu) / 5, 150);
    }


    private void skor_goster(Graphics2D g) {
        g.setFont(yaziBoyutu);
        g.setColor(new Color(97, 184, 0));
        String s = "Puan: " + skor;
        g.drawString(s, ekran_buyuklugu / 2 + 96, ekran_buyuklugu + 16);

        for (int i = 0; i < kalan_can; i++) {
            g.drawImage(kalp, i * 28 + 8, ekran_buyuklugu + 1, this);
        }
    }

    private void labirent_kontrol() {

        int i = 0;
        boolean bitis = true;

        while (i < n_blok * n_blok && bitis) {

            if ((ekranBilgileri[i]) != 0) {
                bitis = false;
            }

            i++;
        }

        if (bitis) {

            skor += 50;

            if (n_hayalet < hayalet_sayisi) {
                n_hayalet++;
            }

            if (hareketHizi < maksHiz) {
                hareketHizi++;
            }

            baslangic_Level();
        }
    }

    private void death() {

        kalan_can--;

        if (kalan_can == 0) {
            oyunDevam = false;
        }

        devam_Level();
    }



    private void hayalet_hareket(Graphics2D g2d) {

        int pozisyon;
        int sayac;

        for (int i = 0; i < n_hayalet; i++) {
            if (hayalet_x[i] % blok_boyutu == 0 && hayalet_y[i] % blok_boyutu == 0) {
                pozisyon = hayalet_x[i] / blok_boyutu + n_blok * (int) (hayalet_y[i] / blok_boyutu);

                sayac = 0;

                if ((ekranBilgileri[pozisyon] & 1) == 0 && hayalet_dx[i] != 1) {
                    dx[sayac] = -1;
                    dy[sayac] = 0;
                    sayac++;
                }

                if ((ekranBilgileri[pozisyon] & 2) == 0 && hayalet_dy[i] != 1) {
                    dx[sayac] = 0;
                    dy[sayac] = -1;
                    sayac++;
                }

                if ((ekranBilgileri[pozisyon] & 4) == 0 && hayalet_dx[i] != -1) {
                    dx[sayac] = 1;
                    dy[sayac] = 0;
                    sayac++;
                }

                if ((ekranBilgileri[pozisyon] & 8) == 0 && hayalet_dy[i] != -1) {
                    dx[sayac] = 0;
                    dy[sayac] = 1;
                    sayac++;
                }

                if (sayac == 0) {

                    if ((ekranBilgileri[pozisyon] & 15) == 15) {
                        hayalet_dx[i] = 0;
                        hayalet_dy[i] = 0;
                    } else {
                        hayalet_dx[i] = -hayalet_dx[i];
                        hayalet_dy[i] = -hayalet_dy[i];
                    }

                } else {

                    sayac = (int) (Math.random() * sayac);

                    if (sayac > 3) {
                        sayac = 3;
                    }

                    hayalet_dx[i] = dx[sayac];
                    hayalet_dy[i] = dy[sayac];
                }

            }

            hayalet_x[i] = hayalet_x[i] + (hayalet_dx[i] * hayaletHiz[i]);
            hayalet_y[i] = hayalet_y[i] + (hayalet_dy[i] * hayaletHiz[i]);
            hayalet_ciz(g2d, hayalet_x[i] + 1, hayalet_y[i] + 1);

            if (pacman_x > (hayalet_x[i] - 12) && pacman_x < (hayalet_x[i] + 12)
                    && pacman_y > (hayalet_y[i] - 12) && pacman_y < (hayalet_y[i] + 12)
                    && oyunDevam) {

                olumDurumu = true;
            }
        }
    }

    private void hayalet_ciz(Graphics2D g2d, int x, int y) {
        g2d.drawImage(hayalet, x, y, this);
    }

    private void pacman_hareket() {

        int pozisyon;
        short ch;

        if (pacman_x % blok_boyutu == 0 && pacman_y % blok_boyutu == 0) {
            pozisyon = pacman_x / blok_boyutu + n_blok * (int) (pacman_y / blok_boyutu);
            ch = ekranBilgileri[pozisyon];

            if ((ch & 16) != 0) {
                ekranBilgileri[pozisyon] = (short) (ch & 15);
                skor++;
            }

            if (req_dx != 0 || req_dy != 0) {
                if (!((req_dx == -1 && req_dy == 0 && (ch & 1) != 0)
                        || (req_dx == 1 && req_dy == 0 && (ch & 4) != 0)
                        || (req_dx == 0 && req_dy == -1 && (ch & 2) != 0)
                        || (req_dx == 0 && req_dy == 1 && (ch & 8) != 0))) {
                    pacmand_x = req_dx;
                    pacmand_y = req_dy;
                }
            }

            // durma durumu kontrol ediliyor
            if ((pacmand_x == -1 && pacmand_y == 0 && (ch & 1) != 0)
                    || (pacmand_x == 1 && pacmand_y == 0 && (ch & 4) != 0)
                    || (pacmand_x == 0 && pacmand_y == -1 && (ch & 2) != 0)
                    || (pacmand_x == 0 && pacmand_y == 1 && (ch & 8) != 0)) {
                pacmand_x = 0;
                pacmand_y = 0;
            }
        }
        pacman_x = pacman_x + pacman_hiz * pacmand_x;
        pacman_y = pacman_y + pacman_hiz * pacmand_y;
    }

    private void pacman_ciz(Graphics2D g2d) {

        if (req_dx == -1) {
            g2d.drawImage(sol, pacman_x + 1, pacman_y + 1, this);
        } else if (req_dx == 1) {
            g2d.drawImage(sag, pacman_x + 1, pacman_y + 1, this);
        } else if (req_dy == -1) {
            g2d.drawImage(yukari, pacman_x + 1, pacman_y + 1, this);
        } else {
            g2d.drawImage(asagi, pacman_x + 1, pacman_y + 1, this);
        }
    }

    private void labirent_ciz(Graphics2D g2d) {

        short i = 0;
        int x, y;

        for (y = 0; y < ekran_buyuklugu; y += blok_boyutu) {
            for (x = 0; x < ekran_buyuklugu; x += blok_boyutu) {

                g2d.setColor(new Color(64, 0, 137));
                g2d.setStroke(new BasicStroke(5));

                if ((seviyeBilgileri[i] == 0)) {
                    g2d.fillRect(x, y, blok_boyutu, blok_boyutu);
                }

                if ((ekranBilgileri[i] & 1) != 0) {
                    g2d.drawLine(x, y, x, y + blok_boyutu - 1);
                }

                if ((ekranBilgileri[i] & 2) != 0) {
                    g2d.drawLine(x, y, x + blok_boyutu - 1, y);
                }

                if ((ekranBilgileri[i] & 4) != 0) {
                    g2d.drawLine(x + blok_boyutu - 1, y, x + blok_boyutu - 1,
                            y + blok_boyutu - 1);
                }

                if ((ekranBilgileri[i] & 8) != 0) {
                    g2d.drawLine(x, y + blok_boyutu - 1, x + blok_boyutu - 1,
                            y + blok_boyutu - 1);
                }

                if ((ekranBilgileri[i] & 16) != 0) {
                    g2d.setColor(new Color(219, 142, 2));
                    g2d.fillOval(x + 10, y + 10, 6, 6);
                }

                i++;
            }
        }
    }

    private void oyun_sifirla() {

        kalan_can = 3;
        skor = 0;
        baslangic_Level();
        n_hayalet = 7;
        hareketHizi = 3;
    }

    private void baslangic_Level() {

        int i;
        for (i = 0; i < n_blok * n_blok; i++) {
            ekranBilgileri[i] = seviyeBilgileri[i];
        }

        devam_Level();
    }


    private void devam_Level() {

        int dx = 1;
        int random;
        int x =  1;
        for (int i = 0; i < n_hayalet; i++) {
            x++;
            hayalet_y[i] = x * blok_boyutu; //Hayalet baslangıc noktası
            hayalet_x[i] = x * blok_boyutu;
            hayalet_dy[i] = 0;
            hayalet_dx[i] = dx;
            dx = -dx;
            random = (int) (Math.random() * (hareketHizi + 1));

            if (random > hareketHizi) {
                random = hareketHizi;
            }

            hayaletHiz[i] = gecerli_hizlar[random];
        }

        pacman_x = 7 * blok_boyutu;  //Pacman baslangıc noktası
        pacman_y = 11 * blok_boyutu;
        pacmand_x = 0;    //yon hareketleri sıfırlanıyor
        pacmand_y = 0;
        req_dx = 0;        //yon kontrolleri sıfırlanıyor
        req_dy = 0;
        olumDurumu = false;
    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, boyut.width, boyut.height);

        labirent_ciz(g2d);
        skor_goster(g2d);

        if (oyunDevam) {
            oyun_oyna(g2d);
        } else {
            girisEkraniGoster(g2d);
        }

        Toolkit.getDefaultToolkit().sync();
        g2d.dispose();
    }


    //kontroller
    class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if (oyunDevam) {
                if (key == KeyEvent.VK_LEFT) {
                    req_dx = -1;
                    req_dy = 0;
                } else if (key == KeyEvent.VK_RIGHT) {
                    req_dx = 1;
                    req_dy = 0;
                } else if (key == KeyEvent.VK_UP) {
                    req_dx = 0;
                    req_dy = -1;
                } else if (key == KeyEvent.VK_DOWN) {
                    req_dx = 0;
                    req_dy = 1;
                } else if (key == KeyEvent.VK_ESCAPE && timer.isRunning()) {
                    oyunDevam = false;
                }
            } else {
                if (key == KeyEvent.VK_SPACE) {
                    oyunDevam = true;
                    oyun_sifirla();
                }
            }
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

}
