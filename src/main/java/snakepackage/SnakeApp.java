package snakepackage;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.*;

import enums.GridSize;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author jd-
 *
 */
public class SnakeApp {

    private Snake winner;

    private Snake noob;

    public Snake getWinner() {
        return winner;
    }

    public void setWinner(Snake winner) {
        this.winner = winner;
    }

    private static SnakeApp app;
    public static final Integer MAX_THREADS = 8;
    Snake[] snakes = new Snake[MAX_THREADS];
    private static final Cell[] spawn = {
        new Cell(1, (GridSize.GRID_HEIGHT / 2) / 2),
        new Cell(GridSize.GRID_WIDTH - 2,
        3 * (GridSize.GRID_HEIGHT / 2) / 2),
        new Cell(3 * (GridSize.GRID_WIDTH / 2) / 2, 1),
        new Cell((GridSize.GRID_WIDTH / 2) / 2, GridSize.GRID_HEIGHT - 2),
        new Cell(1, 3 * (GridSize.GRID_HEIGHT / 2) / 2),
        new Cell(GridSize.GRID_WIDTH - 2, (GridSize.GRID_HEIGHT / 2) / 2),
        new Cell((GridSize.GRID_WIDTH / 2) / 2, 1),
        new Cell(3 * (GridSize.GRID_WIDTH / 2) / 2,
        GridSize.GRID_HEIGHT - 2)};
    private JFrame frame;
    private static Board board;
    private JButton pause;
    private JButton start;

    private JButton resume;
    int nr_selected = 0;
    Thread[] thread = new Thread[MAX_THREADS];

    private Object pauseObject;

    public SnakeApp() {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        frame = new JFrame("The Snake Race");
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // frame.setSize(618, 640);
        frame.setSize(GridSize.GRID_WIDTH * GridSize.WIDTH_BOX + 17,
                GridSize.GRID_HEIGHT * GridSize.HEIGH_BOX + 40);
        frame.setLocation(dimension.width / 2 - frame.getWidth() / 2,
                dimension.height / 2 - frame.getHeight() / 2);
        board = new Board();
        pause = new JButton("Pause");
        resume = new JButton("Resume");
        start = new JButton("start");
        frame.add(board,BorderLayout.CENTER);
        JPanel actionsBPabel=new JPanel();
        actionsBPabel.setLayout(new FlowLayout());
        actionsBPabel.add(pause);
        actionsBPabel.add(resume);
        actionsBPabel.add(start);
        frame.add(actionsBPabel,BorderLayout.SOUTH);
        addEvents();

    }


    private void addEvents(){
        pause.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Pause();
            }
        });


        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Start();
            }
        });


        resume.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Resume();
            }
        });
    }


    private void Pause(){
        for(Snake sn:snakes){
            sn.stop();
        }
        findWinner();

        System.out.println("EL GANADOR POR EL MOMENTO ES : " + winner.getIdt());
        if(noob != null){
            System.out.println("EL PEOR GUSANO ES : " + noob.getIdt());
        }

    }


    private void Start(){
        for (int i = 0; i != MAX_THREADS; i++) {
            thread[i].start();
        }
    }


    private void Resume(){
        for(Snake sn:snakes){
            sn.startGame();
        }
    }

    public static void main(String[] args) {
        app = new SnakeApp();
        app.init();
    }

    private void init() {
        boolean noobSnake = true;
        pauseObject = new Object();
        noob = null;
        for (int i = 0; i != MAX_THREADS; i++) {
            snakes[i] = new Snake(i + 1, spawn[i], i + 1,pauseObject);
            snakes[i].addObserver(board);
            thread[i] = new Thread(snakes[i]);
        }

        frame.setVisible(true);
        while (true) {
            //int x = 0;

            AtomicInteger x = new AtomicInteger(0);
            for (int i = 0; i != MAX_THREADS; i++) {
                if (snakes[i].isSnakeEnd()) {
                    x.getAndIncrement();
                    if(noobSnake){
                        noob = snakes[i];
                        noobSnake = false;
                    }
                }
            }
            if (x.get() == (MAX_THREADS)) {
                break;
            }
        }


        System.out.println("Thread (snake) status:");
        for (int i = 0; i != MAX_THREADS; i++) {
            System.out.println("["+i+"] :"+thread[i].getState());
        }
        

    }


    private void findWinner(){
        Snake winnerPartial = snakes[0];
        int max = snakes[0].getBody().size();
        for(int i = 0; i < MAX_THREADS;i++){
            if(snakes[i].getBody().size() > max){
                winnerPartial = snakes[i];
            }
        }
        setWinner(winnerPartial);
    }

    public static SnakeApp getApp() {
        return app;
    }

}
