package app.view;

import app.model.Color;

public class GameStats {

    public int rounds = 0;

    public int yellowWins = 0;
    public int redWins = 0;
    public int ties = 0;

    public int roundYellowMoves = 0;
    public int roundRedMoves = 0;
    public int totalYellowMoves = 0;
    public int totalRedMoves = 0;

    public int roundYellowTime = 0;
    public int roundRedTime = 0;
    public long totalYellowTime = 0;
    public long totalRedTime = 0;

    public int yellowNodesVisited = 0;
    public int redNodesVisited = 0;
    public int yellowPrunes = 0;
    public int redPrunes = 0;

    public void resetRound() {
        roundYellowMoves = 0;
        roundRedMoves = 0;
        roundYellowTime = 0;
        roundRedTime = 0;

        yellowNodesVisited = 0;
        redNodesVisited = 0;
        yellowPrunes = 0;
        redPrunes = 0;
    }

    public void addMove(Color color, long time) {
        if (color == Color.YELLOW) {
            roundYellowMoves++;
            roundYellowTime += time;
        } else if (color == Color.RED) {
            roundRedMoves++;
            roundRedTime += time;
        }
    }

    public void finishRound(Color winner) {
        rounds++;
        totalYellowMoves += roundYellowMoves;
        totalRedMoves += roundRedMoves;
        totalYellowTime += roundYellowTime;
        totalRedTime += roundRedTime;
        if (winner == null) {
            ties++;
        } else {
            if (winner == Color.YELLOW) {
                yellowWins++;
            } else {
                redWins++;
            }
        }
    }

    public void nodeVisited(Color color) {
        if (color == Color.YELLOW) {
            yellowNodesVisited++;
        } else if (color == Color.RED) {
            redNodesVisited++;
        }
    }

    public void prune(Color color) {
        if (color == Color.YELLOW) {
            yellowPrunes++;
        } else if (color == Color.RED) {
            redPrunes++;
        }
    }

    @Override
    public String toString() {
        return "OSTATNIA RUNDA:" +
                "\nGracz żółty:" +
                "\n\truchy:\t\t\t\t" + roundYellowMoves +
                "\n\tczas działania AI (ms):\t" + roundYellowTime +
                "\n\tczas\\ruch (ms):\t\t\t" + (roundYellowMoves == 0 ? 0 : roundYellowTime / roundYellowMoves) +
                "\n\todwiedzone węzły:\t\t" + yellowNodesVisited +
                "\n\todwiedzone węzły\\ruch:\t" + yellowNodesVisited / roundYellowMoves +
                "\n\tcięcia:\t\t\t\t" + yellowPrunes +
                "\n\tcięcia\\ruch:\t\t\t" + yellowPrunes / roundYellowMoves +
                "\nGracz czerwony:" +
                "\n\truchy:\t\t\t\t" + roundRedMoves +
                "\n\tczas działania AI (ms):\t" + roundRedTime +
                "\n\tczas\\ruch (ms):\t\t\t" + (roundRedMoves == 0 ? 0 : roundRedTime / roundRedMoves) +
                "\n\todwiedzone węzły:\t\t" + redNodesVisited +
                "\n\todwiedzone węzły\\ruch:\t" + redNodesVisited / roundRedMoves +
                "\n\tcięcia:\t\t\t\t" + redPrunes +
                "\n\tcięcia\\ruch:\t\t\t" + redPrunes / roundRedMoves +
                "\n\nWSZYSTKIE RUNDY (" + rounds + "): " +
                "\n\nWygrane żółtego:\t\t" + yellowWins +
                "\nWygrane czerwonego:\t" + redWins +
                "\nRemisy:\t\t\t\t" + ties +
                "\n\nGracz żółty:\t\t\t\tŚrednio:\t\tŁącznie:" +
                "\n\truchy:\t\t\t\t" + totalYellowMoves / rounds + "\t\t\t" + totalYellowMoves +
                "\n\tczas działania AI (ms):\t" + totalYellowTime / rounds + "\t\t\t" + totalYellowTime +
                "\n\tczas\\ruch (ms):\t\t\t" + (totalYellowMoves == 0 ? 0 : totalYellowTime / totalYellowMoves) + "\t\t\t" + (totalYellowMoves == 0 ? 0 : totalYellowTime / totalYellowMoves) +
                "\nGracz czerwony:\t\t\tŚrednio:\t\tŁącznie:" +
                "\n\truchy:\t\t\t\t" + totalRedMoves / rounds + "\t\t\t" + totalRedMoves +
                "\n\tczas działania AI (ms):\t" + totalRedTime / rounds + "\t\t\t" + totalRedTime +
                "\n\tczas\\ruch (ms):\t\t\t" + (totalRedMoves == 0 ? 0 : totalRedTime / totalRedMoves) + "\t\t\t" + (totalRedMoves == 0 ? 0 : totalRedTime / totalRedMoves);
    }
}
