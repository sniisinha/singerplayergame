package quoridor;

import general.Board;
import java.util.*;

public class QuoridorBoard extends Board {

    private final char[][] grid;

    private final List<int[]> pawns = new ArrayList<>();
    private int[] wallsRemaining; // per player
    private Goal[] goals; // per player
    private final char[] symbols = { 'A', 'B', 'C', 'D' };
    private final int numPlayers;

    // goal direction for each player
    private enum Goal {
        TOP, BOTTOM, LEFT, RIGHT
    }

    // cells -> rows*cols
    private final List<List<Integer>> nodes = new ArrayList<>();
    private boolean[][] blockDown; // size: (rows-1) x cols
    private boolean[][] blockRight; // size: rows x (cols-1)

    public QuoridorBoard() {
        this(2);
    }

    public QuoridorBoard(int numPlayers) {
        super(9, 9); // 9x9 by default
        if (numPlayers < 2 || numPlayers > 4)
            throw new IllegalArgumentException("Players must be 2..4");
        this.numPlayers = numPlayers;
        this.grid = new char[rows][cols];
        initialize();
    }

    private void initialize() {
        for (int i = 0; i < rows; i++)
            Arrays.fill(grid[i], '.');

        buildFullAdjacency();
        blockDown = new boolean[rows - 1][cols];
        blockRight = new boolean[rows][cols - 1];

        wallsRemaining = new int[numPlayers];
        goals = new Goal[numPlayers];
        int initWalls = (numPlayers == 2) ? 10 : (numPlayers == 3 ? 8 : 7); // reasonable defaults
        Arrays.fill(wallsRemaining, initWalls);

        int midR = rows / 2;
        int midC = cols / 2;
        pawns.clear();

        // place pawns + goals
        if (numPlayers >= 1) { // P1: top -> bottom
            pawns.add(new int[] { 0, midC });
            goals[0] = Goal.BOTTOM;
        }
        if (numPlayers >= 2) { // P2: bottom -> top
            pawns.add(new int[] { rows - 1, midC });
            goals[1] = Goal.TOP;
        }
        if (numPlayers >= 3) { // P3: left -> right
            pawns.add(new int[] { midR, 0 });
            goals[2] = Goal.RIGHT;
        }
        if (numPlayers >= 4) { // P4: right -> left
            pawns.add(new int[] { midR, cols - 1 });
            goals[3] = Goal.LEFT;
        }

        for (int i = 0; i < numPlayers; i++) {
            int r = pawns.get(i)[0], c = pawns.get(i)[1];
            grid[r][c] = symbols[i];
        }
    }

    private void buildFullAdjacency() {
        nodes.clear();
        int total = rows * cols;
        for (int i = 0; i < total; i++)
            nodes.add(new ArrayList<>());
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int u = rcToId(r, c);
                if (r > 0)
                    addEdge(u, rcToId(r - 1, c));
                if (r < rows - 1)
                    addEdge(u, rcToId(r + 1, c));
                if (c > 0)
                    addEdge(u, rcToId(r, c - 1));
                if (c < cols - 1)
                    addEdge(u, rcToId(r, c + 1));
            }
        }
    }

    private int rcToId(int r, int c) {
        return r * cols + c;
    }

    private int rowOf(int id) {
        return id / cols;
    }

    private int colOf(int id) {
        return id % cols;
    }

    private void addEdge(int a, int b) {
        if (!nodes.get(a).contains(b))
            nodes.get(a).add(b);
        if (!nodes.get(b).contains(a))
            nodes.get(b).add(a);
    }

    private void cutEdge(int a, int b) {
        nodes.get(a).remove(Integer.valueOf(b));
        nodes.get(b).remove(Integer.valueOf(a));
    }

    @Override
    public void display() {
        final int H = rows * 2 - 1;
        final int W = cols * 2 - 1;
        char[][] canvas = new char[H][W];
        for (int i = 0; i < H; i++)
            Arrays.fill(canvas[i], ' ');

        // cells
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                canvas[r * 2][c * 2] = (grid[r][c] == '.') ? '·' : grid[r][c];
            }
        }

        // vertical blocked lines
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols - 1; c++) {
                if (blockRight[r][c]) {
                    canvas[r * 2][c * 2 + 1] = '║';
                }
            }
        }

        // horizontal blocked lines
        for (int r = 0; r < rows - 1; r++) {
            for (int c = 0; c < cols; c++) {
                if (blockDown[r][c]) {
                    canvas[r * 2 + 1][c * 2] = '═';
                }
            }
        }

        // intersections: mark junction if both directions around exist
        for (int r = 0; r < rows - 1; r++) {
            for (int c = 0; c < cols - 1; c++) {
                int cr = r * 2 + 1, cc = c * 2 + 1;
                boolean hasH = blockDown[r][c] || blockDown[r][c + 1];
                boolean hasV = blockRight[r][c] || blockRight[r + 1][c];
                if (hasH && hasV)
                    canvas[cr][cc] = '╬';
            }
        }

        System.out.print("\n      ");
        for (int x = 0; x < W; x++) {
            if (x % 2 == 1) {
                int colIdx = (x - 1) / 2 + 1; // 1..cols-1
                System.out.print(colIdx % 10);
            } else {
                System.out.print(' ');
            }
            System.out.print(' ');
        }
        System.out.println();

        for (int y = 0; y < H; y++) {
            if (y % 2 == 1) {
                int rowIdx = (y - 1) / 2 + 1;
                System.out.printf("  %2d  ", rowIdx);
            } else {
                System.out.print("      ");
            }
            for (int x = 0; x < W; x++) {
                System.out.print(canvas[y][x]);
                if (x < W - 1)
                    System.out.print(' ');
            }
            System.out.println();
        }

        System.out.println("\nWalls remaining:");
        for (int i = 0; i < numPlayers; i++) {
            System.out.println("  Player " + (i + 1) + " (" + symbols[i] + "): " + wallsRemaining[i]);
        }

        System.out.println("Steps remaining for");
        for (int i = 0; i < numPlayers; i++) {
            int steps = bfsMinStepsToGoal(i, /* uiMode= */true);
            System.out.println(
                    "  Player " + (i + 1) + " → " + goalLabel(goals[i]) + ": " + (steps == -1 ? "Blocked" : steps));
        }
    }

    private String goalLabel(Goal g) {
        switch (g) {
            case TOP:
                return "TOP";
            case BOTTOM:
                return "BOTTOM";
            case LEFT:
                return "LEFT";
            case RIGHT:
                return "RIGHT";
        }
        return "";
    }

    public boolean movePawn(int playerId, String direction) {
        int idx = playerId - 1;
        if (idx < 0 || idx >= numPlayers) {
            System.out.println("Invalid player.");
            return false;
        }
        int[] pos = pawns.get(idx);
        int r = pos[0], c = pos[1];
        int nr = r, nc = c;

        switch (direction) {
            case "up":
                nr--;
                break;
            case "down":
                nr++;
                break;
            case "left":
                nc--;
                break;
            case "right":
                nc++;
                break;
            default:
                System.out.println("Invalid direction! Use up/down/left/right.");
                return false;
        }

        if (!isInside(nr, nc)) {
            System.out.println("Move blocked or out of bounds!");
            return false;
        }
        if (occupiedByAny(nr, nc)) {
            System.out.println("Move blocked: target cell is occupied!");
            return false;
        }

        // must be adjacent in the graph
        int u = rcToId(r, c), v = rcToId(nr, nc);
        if (!nodes.get(u).contains(v)) {
            System.out.println("Move blocked by wall!");
            return false;
        }

        // update display
        grid[r][c] = '.';
        grid[nr][nc] = symbols[idx];
        pos[0] = nr;
        pos[1] = nc;
        return true;
    }

    private boolean occupiedByAny(int tr, int tc) {
        for (int i = 0; i < numPlayers; i++) {
            int[] p = pawns.get(i);
            if (p[0] == tr && p[1] == tc)
                return true;
        }
        return false;
    }
    private int bfsMinStepsToGoal(int playerIdx, boolean uiMode) {
        int sr = pawns.get(playerIdx)[0], sc = pawns.get(playerIdx)[1];
        if (hitsGoal(sr, sc, goals[playerIdx]))
            return 0;

        int[][] dist = new int[rows][cols];
        for (int i = 0; i < rows; i++)
            Arrays.fill(dist[i], -1);

        boolean[][] blockedCell = new boolean[rows][cols];
        if (uiMode) {
            for (int i = 0; i < numPlayers; i++)
                if (i != playerIdx) {
                    int[] p = pawns.get(i);
                    blockedCell[p[0]][p[1]] = true;
                }
        }

        int startId = rcToId(sr, sc);
        ArrayDeque<Integer> q = new ArrayDeque<>();
        dist[sr][sc] = 0;
        q.add(startId);

        while (!q.isEmpty()) {
            int u = q.poll();
            int ur = rowOf(u), uc = colOf(u);
            for (int v : nodes.get(u)) {
                int vr = rowOf(v), vc = colOf(v);
                if (dist[vr][vc] != -1)
                    continue;
                if (uiMode && blockedCell[vr][vc])
                    continue;

                dist[vr][vc] = dist[ur][uc] + 1;
                if (hitsGoal(vr, vc, goals[playerIdx]))
                    return dist[vr][vc];
                q.add(v);
            }
        }
        return -1;
    }

    private boolean hitsGoal(int vr, int vc, Goal g) {
        switch (g) {
            case TOP:
                return vr == 0;
            case BOTTOM:
                return vr == rows - 1;
            case LEFT:
                return vc == 0;
            case RIGHT:
                return vc == cols - 1;
        }
        return false;
    }

    // placeLine — block exactly ONE line (edge) chosen by the user.
    public boolean placeLine(int r, int c, String type, int playerId) {
        int idx = playerId - 1;
        if (idx < 0 || idx >= numPlayers) {
            System.out.println("Invalid player.");
            return false;
        }

        if (wallsRemaining[idx] <= 0) {
            System.out.println("Player " + playerId + " has no walls left!");
            return false;
        }

        int rr = r - 1, cc = c - 1; // 0-based
        List<int[]> changedBlocks = new ArrayList<>();
        List<int[]> changedEdges = new ArrayList<>();

        if ("h".equals(type)) {
            if (rr < 0 || rr >= rows - 1 || cc < 0 || cc >= cols) {
                System.out.println("Invalid horizontal line position.");
                return false;
            }
            if (blockDown[rr][cc]) {
                System.out.println("This horizontal line is already blocked.");
                return false;
            }
            int u = rcToId(rr, cc);
            int v = rcToId(rr + 1, cc);

            blockDown[rr][cc] = true;
            cutEdge(u, v);
            changedBlocks.add(new int[] { 0, rr, cc });
            changedEdges.add(new int[] { u, v });

        } else if ("v".equals(type)) {
            if (rr < 0 || rr >= rows || cc < 0 || cc >= cols - 1) {
                System.out.println("Invalid vertical line position.");
                return false;
            }
            if (blockRight[rr][cc]) {
                System.out.println("This vertical line is already blocked.");
                return false;
            }
            int u = rcToId(rr, cc);
            int v = rcToId(rr, cc + 1);

            blockRight[rr][cc] = true;
            cutEdge(u, v);
            changedBlocks.add(new int[] { 1, rr, cc });
            changedEdges.add(new int[] { u, v });

        } else {
            System.out.println("Invalid line type! Use 'h' or 'v'.");
            return false;
        }

        // all players must still have some path to their own goal
        if (!allPlayersReachableAfterWall()) {
            // rollback
            for (int[] e : changedEdges)
                addEdge(e[0], e[1]);
            for (int[] b : changedBlocks) {
                if (b[0] == 0)
                    blockDown[b[1]][b[2]] = false;
                else
                    blockRight[b[1]][b[2]] = false;
            }
            System.out.println("Illegal line (would block a player's path).");
            return false;
        }

        // consume wall
        wallsRemaining[idx]--;
        System.out.println("Line placed @ (" + r + "," + c + ") type=" + type + " by Player " + playerId + ".");
        return true;
    }

    public boolean placeWall(int r, int c, String orientation, int playerId) {
        if ("h".equals(orientation)) {
            if (!(canPlaceLine(r, c, "h") && canPlaceLine(r, c + 1, "h")))
                return false;

            if (!placeLine(r, c, "h", playerId))
                return false;
            if (!placeLine(r, c + 1, "h", playerId)) {
                placeLineRollback(r, c, "h");
                return false;
            }
            int idx = playerId - 1;
            wallsRemaining[idx]++;
            System.out.println("Wall placed (horizontal) at (" + r + "," + c + ").");
            return true;

        } else if ("v".equals(orientation)) {
            if (!(canPlaceLine(r, c, "v") && canPlaceLine(r + 1, c, "v")))
                return false;

            if (!placeLine(r, c, "v", playerId))
                return false;
            if (!placeLine(r + 1, c, "v", playerId)) {
                placeLineRollback(r, c, "v");
                return false;
            }
            int idx = playerId - 1;
            wallsRemaining[idx]++;
            System.out.println("Wall placed (vertical) at (" + r + "," + c + ").");
            return true;
        } else {
            System.out.println("Invalid orientation! Use 'h' or 'v'.");
            return false;
        }
    }

    private boolean canPlaceLine(int r, int c, String type) {
        int rr = r - 1, cc = c - 1; // 0-based
        if ("h".equals(type)) {
            if (rr < 0 || rr >= rows - 1 || cc < 0 || cc >= cols)
                return false;
            if (blockDown[rr][cc])
                return false;
        } else {
            if (rr < 0 || rr >= rows || cc < 0 || cc >= cols - 1)
                return false;
            if (blockRight[rr][cc])
                return false;
        }
        return true;
    }

    // helper to undo the first line if the second fails during placeWall
    private void placeLineRollback(int r, int c, String type) {
        int rr = r - 1, cc = c - 1;
        if ("h".equals(type)) {
            int u = rcToId(rr, cc), v = rcToId(rr + 1, cc);
            blockDown[rr][cc] = false;
            addEdge(u, v);
        } else {
            int u = rcToId(rr, cc), v = rcToId(rr, cc + 1);
            blockRight[rr][cc] = false;
            addEdge(u, v);
        }
    }

    public boolean hasPlayerWon(int playerId) {
        int idx = playerId - 1;
        if (idx < 0 || idx >= numPlayers)
            return false;
        int r = pawns.get(idx)[0], c = pawns.get(idx)[1];
        switch (goals[idx]) {
            case TOP:
                return r == 0;
            case BOTTOM:
                return r == rows - 1;
            case LEFT:
                return c == 0;
            case RIGHT:
                return c == cols - 1;
        }
        return false;
    }

    // all players must still have a path
    private boolean allPlayersReachableAfterWall() {
        for (int i = 0; i < numPlayers; i++) {
            if (bfsMinStepsToGoal(i, /* uiMode= */false) == -1)
                return false;
        }
        return true;
    }

    public boolean isPathAvailable() {
        return allPlayersReachableAfterWall();
    }
}
