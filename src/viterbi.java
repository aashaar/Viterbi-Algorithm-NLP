public class viterbi
{
    public static void main(String[] args)
    {
        //TODO: input sequence:
        String[] inputs = {"331","122313","331123312"};
        //String[] inputs = {"331123312"};

        for(String input: inputs)
        {
            int[] observationArray = new int[input.length()+1]; // length +1: we start putting values from index 1
            for(int i = 0; i< input.length(); i++)
            {
                int obs = Integer.parseInt(input.charAt(i) +"");
                if(! (obs == 1||obs ==2 || obs ==3)) // incorrect values in sequence
                {
                    System.out.println("ERROR: Input string sequence can only consist of values '1', '2' or '3'");
                    return;
                }
                else
                {
                    observationArray[i+1] = obs; // put value in observationArray. Start from index 1.
                }
            }


            /*
            print the observation array:
            for(int i:observationArray)
            {
                System.out.println(i);
            }*/
            double[][] likelihoodProbabilities = getLikelihoodProbabilities();
            double[][] transitionProbabilities = getTransitionProbabilities();

            //call the viterbi algorithm:
            System.out.println("For input:"+ input);
            viterbi(observationArray,likelihoodProbabilities,transitionProbabilities);
            System.out.println("********************************\n");
        }
    }

    //TODO: function to run viterbi's trellis:
    private static void viterbi(int[] observationsArray, double[][] likelihoodProbabilities, double[][] transitionProbabilities)
    {
        String bestSequence = "";
        double bestPrevComputedValue =0;
        int bestLastState = 0;
        int noOfStates = transitionProbabilities.length - 1; // subtract 1 to balance null index 0 entries
        int[][] backPointers = new int[observationsArray.length][transitionProbabilities.length];
        double viterbi[][] = new double[observationsArray.length][transitionProbabilities.length];
        //viterbi[i][j] = best probability of state[j] given observation i ; state[1] = hot, state[2] = cold

        //initialization step:
        for(int i=1; i<=noOfStates;i++)
        {
            double liklihoodProb = likelihoodProbabilities[observationsArray[1]][i];
            double transProb = transitionProbabilities[0][i];
            viterbi[1][i] = liklihoodProb * transProb;
        }

        //recursion step:
        for(int o = 2; o<observationsArray.length; o++)
        {
            int observation = observationsArray[o];
            for(int i=1;i<=noOfStates;i++)
            {
                int currentState = i;
                int bestPrevState = 0;
                int prevState = 0;
                double maxValue = 0;
                double prevComputedValue = 0;
                double transitionValue = 0;
                double likelihoodValue = 0;
                double computedValue =0;
                for(int j =1 ; j <= noOfStates;j++)
                {
                    prevState = j;
                    transitionValue = transitionProbabilities[prevState][currentState];
                    likelihoodValue = likelihoodProbabilities[observation][currentState];
                    prevComputedValue = viterbi[o-1][prevState];
                    computedValue = prevComputedValue * transitionValue * likelihoodValue;

                    if(maxValue < computedValue)
                    {
                        maxValue = computedValue;
                        bestPrevState = prevState;
                    }
                }

                viterbi[o][currentState] = maxValue;
                backPointers[o][currentState] = bestPrevState;
            }
        }

        //TODO: find maxValue & its state:
        for(int i =1;i<= noOfStates;i++)
        {
            if(bestPrevComputedValue < viterbi[observationsArray.length - 1] [i])
            {
                bestLastState = i;
                bestPrevComputedValue = viterbi[observationsArray.length - 1] [i];

            }
        }

        //TODO: getting the possible sequence going reverse:

        for(int j = backPointers.length - 1; j>=0; j--)
        {
            if(bestLastState == 1) // 1 is 'H'
            {
                bestSequence = "H --> " +bestSequence;
            }
            else if(bestLastState == 2) // 2 is 'C'
            {
                bestSequence = "C --> " + bestSequence;
            }

            bestLastState = backPointers[j][bestLastState];
        }
        System.out.println("Best possible sequence:");
        System.out.println(bestSequence.substring(0,bestSequence.length() - 5));
        //substring to prevent printing '-->' from the last state value
        System.out.println("Probability= "+bestPrevComputedValue);

    }


    //TODO: function to get likelihood probabilities.
    //hardcoded values as per question
    private static double[][] getLikelihoodProbabilities() {
        double[][] probabilities = new double[4][3];
        //ignore the row 0 - use 1,2,3 rows for observations to avoid confusion.
        //prob[i][j] = probability of observation[i] in state j where i could be either 1,2 or 3.
        // for state 1:
        probabilities[1][1] = 0.2;
        probabilities[2][1] = 0.4;
        probabilities[3][1] = 0.4;
        //for state 2:
        probabilities[1][2] = 0.5;
        probabilities[2][2] = 0.4;
        probabilities[3][2] = 0.1;

        return probabilities;
    }

    //TODO: function to get conditional probabilities.
    //hardcoded values as per question
    private static double[][] getTransitionProbabilities()
    {
        double[][] probabilities = new double[3][3];
        //prob[i][j] = Probability of state 'j' given state 'i'

        //for initial state - 0
        probabilities[0][1] = 0.8; // 1|0
        probabilities[0][2] = 0.2; // 2|0

        probabilities[1][1] = 0.7;
        probabilities[1][2] = 0.3;

        probabilities[2][1] = 0.4;
        probabilities[2][2] = 0.6;

        return probabilities;
    }


}
