/*@author Anwar Saleeby
 *@version final
 *@contribs none
 *@date 7/27/15
 * 
 * 
 *        TicTacToe.java: My take on the game of old Tic Tac Toe, using JavaFX.
 * 
 *  
 *  
 */
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.util.Duration;




public class TicTacToe extends Application 
{
	private boolean abletoplay = true;
	private boolean turnX = true;
	private Tile[][] board = new Tile [3][3];
	private List<Combo> combos = new ArrayList<>();
	private Pane base = new Pane();
	
	/*
	 * Base constructor to create the application frame.
	 * 
	 */
	private Parent createContent()
	{
		
		base.setPrefSize(600,600);
		
		for (int x = 0; x < 3; x++){
			for(int o = 0; o < 3; o++){
				Tile tile = new Tile();
				tile.setTranslateX(x * 200);
				tile.setTranslateY(o * 200);
				
				base.getChildren().add(tile);
				
				board[x][o] = tile;
			}
		}
		
		//Horizontal Strikethrough
		for (int y = 0; y < 3; y++){
			combos.add(new Combo(board[0][y], board[1][y], board[2][y]));
		}
		
		//Vertical Strikethrough
		for (int x = 0; x < 3; x++){
			combos.add(new Combo(board[x][0], board[x][1], board[x][2]));
		}
		
		//Diagonal Strikethrough
		combos.add(new Combo(board[0][0], board[1][1], board[2][2]));
		combos.add(new Combo(board[2][0], board[1][1], board[0][2]));
		
		return base; 
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception
	{
		primaryStage.setScene(new Scene(createContent()));
		primaryStage.show();
	}
	
	/*
	 * method to see when the game is over. 
	 * Uses an enhanced for loop for checking the ArrayList of combos.  
	 * 
	 */
	private void checkState() 
	{
		for(Combo combo : combos){
			if(combo.isComplete()){
				abletoplay = false;
				playWinAnimation(combo);
				break;
			}
		}
	}
	
	/*
	 * method for the StrikeThrough Animation  
	 */
	private void playWinAnimation(Combo combo){
		Line line = new Line();
		line.setStartX(combo.tiles[0].getCenterX());
		line.setStartY(combo.tiles[0].getCenterY());
		line.setEndX(combo.tiles[0].getCenterX());
		line.setEndY(combo.tiles[0].getCenterY());
		
		base.getChildren().add(line);
		
		Timeline timeline = new Timeline();	
		timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(2),
				new KeyValue(line.endXProperty(), combo.tiles[2].getCenterX()),
				new KeyValue(line.endYProperty(), combo.tiles[2].getCenterY())));
		timeline.play();
				
	}
	
	/*
	 *  Class for determining whether or not you have won the game.
	 */
	private class Combo
	{
		private Tile[] tiles;
		public Combo(Tile...tiles){
			this.tiles = tiles; 
		}
		public boolean isComplete()
		{
			if (tiles[0].getValue().isEmpty())
				return false;
			return tiles[0].getValue().equals(tiles[1].getValue())
					&& tiles[0].getValue().equals(tiles[2].getValue());
		}
	}
	
	/*
	 * Class to identify each square as a clickable pane.
	 * Also identifies the buttonlayout for X's and O's.
	 * 
	 */
	private class Tile extends StackPane
	{
		private Text text = new Text();
		
		public Tile()
		{
			Rectangle border = new Rectangle(200,200);
			border.setFill(null);
			border.setStroke(Color.GREEN);
			
			text.setFont(Font.font(72));
			
			setAlignment(Pos.CENTER);
			getChildren().addAll(border, text); 
			
			setOnMouseClicked(event -> {
				if(!abletoplay)
					return; 
				
				if(event.getButton() == MouseButton.PRIMARY)
				{
					if(!turnX)
						return;
					
					
					drawX();
					turnX = false;
					checkState();
				}
				
				else if (event.getButton() == MouseButton.SECONDARY){
					if(turnX)
						return;
					
					drawO();
					turnX = true;
					checkState();
				}
			});
			
		}
		//Used as our start point for the Horizontal Strikethough.
		public double getCenterX()
		{
			return getTranslateX() + 100;
		}
		//Used as our value for the Vertical Strikethrough.
		public double getCenterY()
		{
			return getTranslateY() +100;
		}
		//Shows the text on the pane. 
		public String getValue()
		{
			return text.getText(); 
		}
		private void drawX(){
			text.setText("X");
			
		}
		private void drawO(){
			text.setText("O");
		}
		
	}
	
	
	public static void main(String[] args)
	{
		launch(args);

	}

}
