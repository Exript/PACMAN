import javax.swing.*;


class Pacman extends JFrame {

        public Pacman() {
                add(new Model());
        }
    public static void main(String[] args) {
        Pacman pac = new Pacman();
        pac.setVisible(true);
        pac.setTitle("Pacman Oyun");
        pac.setSize(400,430);
        pac.setDefaultCloseOperation(EXIT_ON_CLOSE);
        pac.setLocationRelativeTo(null);
    }
}
