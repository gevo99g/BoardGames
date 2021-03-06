package Checkers;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.scene.control.Label;

import util.Controller;
import util.Users;

import java.awt.*;
import javafx.event.Event;

public class CheckersController implements Controller {



    @FXML
    private GridPane PieceGrids;
    @FXML
    private Label Player1;
    @FXML
    private Label Player2;

    @FXML
    private Label Score1;
    @FXML
    private Label Score2;


    private Checkers game;
        private String playerturn;

        public CheckersController(){
            game = new Checkers();
            playerturn = game.currentplayer;
        }

        public void setPlayers(Users player1, Users player2) {
            Player1.setText(player1.getName());
            Player2.setText(player2.getName());
        }

        public void handleButtonClicked(Event event){

        }
        public void boardClicked(MouseEvent e){

            Node clickNode = e.getPickResult().getIntersectedNode();
            int colIndex = PieceGrids.getColumnIndex(clickNode);
            int rowIndex = PieceGrids.getRowIndex(clickNode);
            // game.scoreBoard(game.whiteplayer)
            // game.scoreBoard(game.redplayer)

            if((clickNode instanceof Circle || clickNode instanceof ImageView)&&
                    clickNode.getId().equals(turnID())) {
                game.getAllPossibleMoves(rowIndex,colIndex);
                System.out.println(game.possibleMoves);
                for (Node n : PieceGrids.getChildren()) {
                    n.setOnMouseClicked(ee -> {
                        if (game.checkMove(PieceGrids.getRowIndex(n), PieceGrids.getColumnIndex(n))) {
                            this.makeMove(clickNode, rowIndex, colIndex, PieceGrids.getRowIndex(n), PieceGrids.getColumnIndex(n));
                            playerturn = game.currentplayer;
                            game.possibleMoves.clear();
                        }
                    });
                }

            }

            Score1.setText(Integer.toString(game.scoreBoard(game.whiteplayer)));
            Score2.setText(Integer.toString(game.scoreBoard(game.redplayer)));


        }


        public void makeMove(Node clickNode,int fr, int fc, int sr, int sc){

            if(sr == game.kingRow()){
                PieceGrids.getChildren().remove(clickNode);
                this.makeKing(sc, sr);
            }
            else{
                PieceGrids.setColumnIndex(clickNode, sc);
                PieceGrids.setRowIndex(clickNode, sr);
            }
            Node n = deletePieceBetween(fr, fc,sr,sc);
            if (n!= null){
                PieceGrids.getChildren().remove(n);
            }
            game.makeMove(fr,fc,sr,sc);

        }

        public Node deletePieceBetween(int fr, int fc, int sr, int sc){
            //If a piece hops over an opposing piece, this deletes that piece hopped

            int midcol = (int)Math.ceil((fc+sc)/2);
            int midrow = (int)Math.ceil((fr+sr)/2);

            for(Node n: PieceGrids.getChildren()){
                if((midcol!= fc && midcol != sc) && (midrow != fr && midrow != sr) &&
                        GridPane.getColumnIndex(n) == midcol && GridPane.getRowIndex(n) == midrow &&
                        (n instanceof Circle || n instanceof ImageView)){
                    return n;
                }
            }
            return null;
        }
        public String turnID(){
            //Finds the ID of the player turn
            System.out.println(playerturn);
            if(playerturn.equals("W")){
                return "White";
            }
            return "Red";
        }

        public void makeKing(int tempc, int tempr){
            //This method sets the crown image of the turn and sets its ID to the corresponding color
            javafx.scene.image.Image img = new javafx.scene.image.Image(getClass().getResource("/view/RedCrown.png").toString());
            if(playerturn == "W"){
                img = new Image(getClass().getResource("/view/WhiteCrown.png").toString());
            }
            ImageView crown = new ImageView(img);
            crown.setFitHeight(50);
            crown.setFitWidth(50);
            crown.setId(turnID());
            PieceGrids.add(crown, tempc, tempr);
        }
    }