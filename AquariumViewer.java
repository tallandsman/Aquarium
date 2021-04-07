/**
 * AquariumViewer represents an interface for playing a game of Aquarium.
 *
 * @Author Tal Ashkenazy Landsman(22981003), John Pillai(22983679) 
 * @Version 2020, 1.0
 */
import java.awt.*;
import java.awt.event.*; 
import javax.swing.SwingUtilities;
import java.util.Arrays;

public class AquariumViewer implements MouseListener
{
    private final int BOXSIZE = 40;          // the size of each square
    private final int OFFSET  = BOXSIZE * 2; // the gap around the board
    private       int WINDOWSIZE;            // set this in the constructor 
    
    private Aquarium puzzle; // the internal representation of the puzzle
    private int        size; // the puzzle is size x size
    private SimpleCanvas sc; // the display window
    
    private Color backCol = Color.WHITE; // background colour
    private Color gridCol = Color.BLACK; // grid colour
    private Color fontCol = Color.BLACK; // text colour
    private Color aqCol = new Color(217,39,39); // aquarium colour
    private Color waterCol = new Color(178,236,255); // water colour
    private Color emptyCol = Color.WHITE; // empty colour
    private Color airCol = Color.PINK; // air colour
   
    /**
     * Main constructor for objects of class AquariumViewer.
     * Sets all fields, and displays the initial puzzle.
     */
    public AquariumViewer(Aquarium puzzle)
    {
        this.puzzle = puzzle;
        size = puzzle.getSize();
        WINDOWSIZE = size * BOXSIZE + OFFSET * 2;
        sc = new SimpleCanvas("Aquarium", WINDOWSIZE, WINDOWSIZE, backCol); 
        sc.setFont(new Font("Calibri", Font.PLAIN, 18));
        sc.addMouseListener(this);
        displayPuzzle();
    }
    
    /**
     * Selects from among the provided files in folder Examples. 
     * xyz selects axy_z.txt. 
     */
    public AquariumViewer(int n)
    {
        this(new Aquarium("Examples/a" + n / 10 + "_" + n % 10 + ".txt"));
    }
    
    /**
     * Uses the provided example file on the LMS page.
     */
    public AquariumViewer()
    {
        this(61);
    }
    
    /**
     * Returns the current state of the puzzle.
     */
    public Aquarium getPuzzle()
    {
        return puzzle;
    }
    
    /**
     * Returns the size of the puzzle.
     */
    public int getSize()
    {
        return size;
    }

    /**
     * Returns the current state of the canvas.
     */
    public SimpleCanvas getCanvas()
    {
        return sc;
    }
    
    /**
     * Displays the initial puzzle; see the LMS page for the format.
     */
    private void displayPuzzle()
    {
       displayGrid();
       displayNumbers();
       displayAquariums();
       displayButtons();   
    }
    
    /**
     * Displays the grid in the middle of the window.
     */
    public void displayGrid()
    {
        for (int i = 0; i <= size; i++)
            sc.drawLine(i * BOXSIZE + OFFSET, OFFSET, i * BOXSIZE + OFFSET, size * BOXSIZE + OFFSET, gridCol);
        for (int j = 0; j <= size; j++)
            sc.drawLine(OFFSET, j * BOXSIZE + OFFSET, size * BOXSIZE + OFFSET, j * BOXSIZE + OFFSET, gridCol);
    }
    
    /**
     * Displays the numbers around the grid.
     */
    public void displayNumbers()
    {
        for (int i = 0; i < size; i++)
        sc.drawString(puzzle.getColumnTotals()[i], i* BOXSIZE + OFFSET + 20, OFFSET - 10, fontCol);
        for (int j = 0; j < size; j++)
        sc.drawString(puzzle.getRowTotals()[j], OFFSET - 20, j* BOXSIZE + OFFSET + 25, fontCol);
    }
    
    /**
     * Displays the aquariums.
     */
    public void displayAquariums()
    {
        sc.drawRectangle(OFFSET, OFFSET, size * BOXSIZE + OFFSET,OFFSET + 3, aqCol);
        sc.drawRectangle(OFFSET, OFFSET, OFFSET + 3, size * BOXSIZE + OFFSET, aqCol);
        sc.drawRectangle(OFFSET, size * BOXSIZE + OFFSET, size * BOXSIZE + OFFSET + 3, size * BOXSIZE + OFFSET + 3, aqCol);
        sc.drawRectangle(size * BOXSIZE + OFFSET, OFFSET, size * BOXSIZE + OFFSET + 3, size * BOXSIZE + OFFSET, aqCol);
        for (int i = 0; i < size; i++)
        for (int j = 0; j < size-1; j++){
        if(puzzle.getAquariums()[i][j] != puzzle.getAquariums()[i][j+1]){
            sc.drawRectangle((j+1) * BOXSIZE + OFFSET, i * BOXSIZE + OFFSET, 
                             (j+1) * BOXSIZE + OFFSET + 3, (i+1) * BOXSIZE + OFFSET, aqCol);            
         }
        }
        for (int i = 0; i < size-1; i++)
        for (int j = 0; j < size; j++){
        if(puzzle.getAquariums()[i][j] != puzzle.getAquariums()[i+1][j]){
            sc.drawRectangle((j) * BOXSIZE + OFFSET, (i+1) * BOXSIZE + OFFSET, 
                             (j+1) * BOXSIZE + OFFSET + 3, (i+1) * BOXSIZE + OFFSET + 3, aqCol); 
         }
        }
    }
    
    /**
     * Displays the buttons below the grid.
     */
    public void displayButtons()
    {
        sc.drawString("SOLVED ?", OFFSET, (size + 1) * BOXSIZE + OFFSET, fontCol);
        sc.drawString("CLEAR", (size-1) * BOXSIZE + OFFSET, (size + 1) * BOXSIZE + OFFSET, fontCol);
    }
    
    /**
     * Updates the display of Square r,c. 
     * Sets the display of this square to whatever is in the squares array. 
     */
    public void updateSquare(int r, int c)
    {
        if (puzzle.getSpaces()[r][c] == Space.WATER){
            sc.drawRectangle( c * BOXSIZE + OFFSET + 3, r * BOXSIZE + OFFSET + 3, 
                             (c+1) *BOXSIZE + OFFSET, (r+1) * BOXSIZE + OFFSET, waterCol);
        }
        if (puzzle.getSpaces()[r][c] == Space.EMPTY){
            sc.drawRectangle( c * BOXSIZE + OFFSET + 3, r * BOXSIZE + OFFSET + 3, 
                             (c+1) *BOXSIZE + OFFSET, (r+1) * BOXSIZE + OFFSET, emptyCol);
        }
        if (puzzle.getSpaces()[r][c] == Space.AIR){
            sc.drawDisc(c * BOXSIZE + OFFSET + BOXSIZE/2 + 1, r * BOXSIZE + OFFSET + BOXSIZE/2 + 1, 15, airCol);   
        }
    }
    
    /**
     * Responds to a mouse click. 
     * If it's on the board, make the appropriate move and update the screen display. 
     * If it's on SOLVED?,   check the solution and display the result. 
     * If it's on CLEAR,     clear the puzzle and update the screen display. 
     */
    public void mousePressed(MouseEvent e) 
    {
         int x = (e.getY() - OFFSET) / BOXSIZE; 
         int y = (e.getX() - OFFSET) / BOXSIZE;
         String solution = CheckSolution.isSolution(puzzle);
         if (OFFSET < e.getX() && OFFSET < e.getY() &&  
             e.getX() < size * BOXSIZE + OFFSET && e.getY() < size * BOXSIZE + OFFSET){
               if (SwingUtilities.isLeftMouseButton(e)){
                   puzzle.leftClick(x,y); 
                   updateSquare(x,y);
               }
                if (SwingUtilities.isRightMouseButton(e)){
                   puzzle.rightClick(x,y); 
                   updateSquare(x,y);
               }
         }
         if (OFFSET < e.getX() && size * BOXSIZE + OFFSET + 20 < e.getY() &&  
             e.getX() < BOXSIZE + OFFSET && e.getY() < (size+1) * BOXSIZE + OFFSET){
              if(!Arrays.equals(puzzle.getRowTotals(),CheckSolution.rowCounts(puzzle)) ||
                 !Arrays.equals(puzzle.getColumnTotals(),CheckSolution.columnCounts(puzzle))){
                sc.drawRectangle(OFFSET + 60 , (size) * BOXSIZE + OFFSET + 45, 
                                 OFFSET + (size + 2) * BOXSIZE , (size + 1) * BOXSIZE + OFFSET + 30 ,backCol);
                sc.drawString(solution, OFFSET + size * 10 , (size + 1) * BOXSIZE + OFFSET + 20 , fontCol);
              }       
              for (int t = 1; t <= puzzle.getAquariums()[puzzle.getSize()-1][puzzle.getSize()-1]; t++)
              if(!CheckSolution.isAquariumOK(puzzle,t).equals("")){
                sc.drawRectangle(OFFSET + 60 , (size) * BOXSIZE + OFFSET + 45, 
                                 OFFSET + (size + 2) * BOXSIZE , (size + 1) * BOXSIZE + OFFSET + 30 ,backCol);
                sc.drawString(solution, OFFSET + size * 10 , (size + 1) * BOXSIZE + OFFSET + 20 , fontCol);
                }
              if(solution.equals("\u2713\u2713\u2713")){
                 sc.drawRectangle(OFFSET + 60 , (size) * BOXSIZE + OFFSET + 45, 
                                  OFFSET + (size + 2) * BOXSIZE , (size + 1) * BOXSIZE + OFFSET + 30 ,backCol);
                 sc.drawString("Solution Correct!", OFFSET + size * 10 ,
                              (size + 1) * BOXSIZE + OFFSET + 20 , fontCol);
                 sc.drawString("\u2713\u2713\u2713", OFFSET, (size + 1) * BOXSIZE + OFFSET + 20 , fontCol);
                }
         }
         if ((size-1) * BOXSIZE + OFFSET < e.getX() && size * BOXSIZE + OFFSET + 20 < e.getY() &&  
              e.getX() < (size+1) * BOXSIZE + OFFSET && e.getY() < (size+1) * BOXSIZE + OFFSET){
              puzzle.clear();
              for(int i = 0; i < size; i++)
              for(int j = 0; j < size; j++){
                  updateSquare(i,j);
              }
         }
    }
    public void mouseClicked(MouseEvent e) {}
    /**
     * An extension,
     * If the number of water spaces is bigger then the required number in the specific
     * column or row the specific column or row number will turn to red .
     */
    public void mouseReleased(MouseEvent e) {
        for(int i = 0; i < size; i++){
         if(puzzle.getRowTotals()[i] < CheckSolution.rowCounts(puzzle)[i]){
            sc.drawString(puzzle.getRowTotals()[i], OFFSET - 20, i* BOXSIZE + OFFSET + 25, Color.RED);
         } else {
            sc.drawString(puzzle.getRowTotals()[i], OFFSET - 20, i* BOXSIZE + OFFSET + 25, fontCol);}
         if(puzzle.getColumnTotals()[i] < CheckSolution.columnCounts(puzzle)[i]){
            sc.drawString(puzzle.getColumnTotals()[i], i* BOXSIZE + OFFSET + 20, OFFSET - 10, Color.RED);
         } else {
            sc.drawString(puzzle.getColumnTotals()[i], i* BOXSIZE + OFFSET + 20, OFFSET - 10, fontCol);}
         }
     }
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
}
