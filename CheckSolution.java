/**
 * CheckSolution is a utility class which can check if
 * a board position in an Aquarium puzzle is a solution.
 *
 * @Tal Ashkenazy Landsman(22981003), John Pillai(22983679) 
 * @2020, 1.0
 */
import java.util.Arrays; 

public class CheckSolution
{
    /**
     * Non-constructor for objects of class CheckSolution
     */
    private CheckSolution(){}
    
    /**
     * Returns the number of water squares in each row of Aquarium puzzle p, top down.
     */
    public static int[] rowCounts(Aquarium p)
    {
        int[] rowWaterTotals = new int [p.getSize()];
        for(int i = 0; i < p.getSize(); i++)
        for(int j = 0; j < p.getSize(); j++){
             if(p.getSpaces() [i][j] == Space.WATER){
                  rowWaterTotals[i] += 1;
              }
            }
        return rowWaterTotals;
    }
    
    /**
     * Returns the number of water squares in each column of Aquarium puzzle p, left to right.
     */
    public static int[] columnCounts(Aquarium p)
    {
        int[] columnWaterTotals = new int [p.getSize()];
        for(int i = 0; i < p.getSize(); i++)
        for(int j = 0; j < p.getSize(); j++){
             if(p.getSpaces() [i][j] == Space.WATER){
                  columnWaterTotals[j] += 1;
              }
            }
        return columnWaterTotals;
    }
    
    /**
     * Returns a 2-int array denoting the collective status of the spaces 
     * in the aquarium numbered t on Row r of Aquarium puzzle p. 
     * The second element will be the column index c of any space r,c which is in t, or -1 if there is none. 
     * The first element will be: 
     * 0 if there are no spaces in t on Row r; 
     * 1 if they're all water; 
     * 2 if they're all not-water; or 
     * 3 if they're a mixture of water and not-water. 
     */
    public static int[] rowStatus(Aquarium p, int t, int r)
    {
        int water = 0;
        int noWater = 0;
        int[] rowStatus = new int[2];
        rowStatus[1] = -1; 
        for(int c = 0; c < p.getSize(); c++)
         if(p.getAquariums()[r][c] == t){
            if(p.getSpaces()[r][c] == Space.WATER){
               water = 1;  
            }
            if(p.getSpaces()[r][c] == Space.AIR || p.getSpaces()[r][c] == Space.EMPTY){
               noWater = 2 ;  
            }
            rowStatus[1] = c;
        }
        int z = noWater + water;
        rowStatus[0] = z;
        return rowStatus;
    }
    
    /**
     * Returns a statement on whether the aquarium numbered t in Aquarium puzzle p is OK. 
     * Every row must be either all water or all not-water, 
     * and all water must be below all not-water. 
     * Returns "" if the aquarium is ok; otherwise 
     * returns the indices of any square in the aquarium, in the format "r,c". 
     */
    public static String isAquariumOK(Aquarium p, int t)
    {
        boolean isWater = false;
        for (int r = 0; r < p.getSize(); r++){
         if (rowStatus(p,t,r)[0] == 1){
             isWater = true;
         }
         if (rowStatus(p,t,r)[0] == 2 && isWater == true){
             return r + "," + rowStatus(p,t,r)[1];
            }
         if (rowStatus(p,t,r)[0] == 3){
             return r + "," + rowStatus(p,t,r)[1];
            }
        }
        return "";
    }
    
    /**
     * Returns a statement on whether we have a correct solution to Aquarium puzzle p. 
     * Every row and column must have the correct number of water squares, 
     * and all aquariums must be OK. 
     * Returns three ticks if the solution is correct; 
     * otherwise see the LMS page for the expected results. 
     */
    public static String isSolution(Aquarium p)
    {
        if(!Arrays.equals(p.getRowTotals(),rowCounts(p))){
            return "Row " + Arrays.mismatch(p.getRowTotals(),rowCounts(p)) + " is wrong";
        }
        if(!Arrays.equals(p.getColumnTotals(),columnCounts(p))){
            return "Column " + Arrays.mismatch(p.getColumnTotals(),columnCounts(p)) + " is wrong";
        }
        for (int t = 1; t <= p.getAquariums()[p.getSize()-1][p.getSize()-1]; t++){
        if(!isAquariumOK(p,t).equals("")){
           return "The aquarium at " + isAquariumOK(p,t) + " is wrong";
        }
        }
        return "\u2713\u2713\u2713";
    }
}
