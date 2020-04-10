class TicTacToe { 
    int [] rows;
    int [] columns;
    int n;
    int diagonal=0;
    int antidiagonal=0;
    

    /** Initialize your data structure here. */
    public TicTacToe(int n) {
        this.n=n;
        this.rows=new int[n];
        this.columns=new int[n];
        
    }
    
    /** Player {player} makes a move at ({row}, {col}).
        @param row The row of the board.
        @param col The column of the board.
        @param player The player, can be either 1 or 2.
        @return The current winning condition, can be either:
                0: No one wins.
                1: Player 1 wins.
                2: Player 2 wins. */
    public int move(int row, int col, int player) {
        int value=(player==1)? 1:-1;
        rows[row]+=value;
        columns[col]+=value;
        if(row==col){
            diagonal+=value;
        }
        if(row+col==columns.length+1){
            antidiagonal+=value;
        }
        int size=rows.length;
        if (Math.abs(rows[row]) == size ||
            Math.abs(columns[col]) == size ||
            Math.abs(diagonal) == size  ||
            Math.abs(antidiagonal) == size) {
            return player;
        }

        return 0;
    }
}