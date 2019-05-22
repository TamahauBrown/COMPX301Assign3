/**
 * REcompiler
 * ==========================
 * Tamahau Brown - 1314934
 * Troy Dean - 0222566
 */
class REcompiler
{
    boolean starStart = false;
    int num = 0;
    String p [];
    int j = 0;
    int state = 0;
    int checkQuotes = 0;
    boolean isBracket = false;
    boolean firstTime = true;
    
    public static void main(String [] args)
    {
        REcompiler re = new REcompiler();
        
        //Gets regexp pattern as an arg
        String regexp = args[0];
        
        //Creates an array with the size of the argument the user provided
        re.p = new String[regexp.length()];
        
        //Adds in the symbol at the specific position that it appeared in the argument to the array.
        for(int i = 0; i < regexp.length(); i++)
        {
            re.p[i] = Character.toString(regexp.charAt(i));
        }
        
        //Runs throught the FSM to create the current and next states
        re.Expression();
    }

    /*
        Gets anything that is an expression in the FSM and constructs the necessary Factors and Terms needed.
    */
    public void Expression()
    {
        //On the first iteration through the FSM it will create the starting point which will be represented with a '.' to mean an empty character
        if(firstTime)
        {
            //Checks that there is more than one character in the FSM to search for special cases
            if(j+1 < p.length)
            {
                //If a '*', '?' or '|' is the 2nd character it sets the first point that the FSM goes to, to that position
                if(p[j+1].equals("*") || p[j+1].equals("?") || p[j+1].equals("|"))
                {
                    set_State(-1, ".", j+2, j+2);
                    starStart = true;
                }
                //If no special cases are provided it just makes it go to the first literal or symbol in the FSM
                else
                {
                    set_State(-1, ".", j+1, j+1);
                }
            }
            //IF the FSM is not big enough it is assumed there is only a literal in the machine so it points to that position
            else
            {
                set_State(-1, ".", j+1, j+1);
            }
            firstTime = false;
        }
        
        //If there are still values in the array to be indexed it will progress through the FSM, otherwise it will finish and stop creating the FSM
        if(num != -1)
        {
            //Cover E --> T
            Term();
            
            //IF after the terms it is not able to continue the expression due to ArrayOutOfBounds it will terminate immediately
            if(j > p.length - 1)
            {
                System.out.println("EXIT");
                return;
            }
            
        //If it is a new line it will reiterate through the FSM and create another Expression
        if(!(p[j].equals("\n")))
           {
            //Covers E --> TE
            Expression();  
           }
        }
        //Otherwise it is assumed to be at the end and terminates the FSM
        else
        {
            System.out.println("Exiting Parser");
            return;
        }
    }

    /*
        Any case that is not defined as an Expression or a Factor will be checked and created in here, this includes: *, ? [], ^[]
    */
    public void Term()
    {
        //If it is outside of the array it terminates immediately
        if(num == -1)
        {
            return;
        }
        
        //This is the closure case which makes the item go through the state zero or more times
        if(p[j].equals("*"))
        {
            
            //Covers the * case
            set_State(j, p[j], j, j+2);
            j++;
            return;
        }
        
        //The set or case, which checks each item to see if it is that value, if it is not then it goes to the next item to check if it is.
        if(p[j].equals("["))
        {
            j++;
            j = orSets(j);
            return;
        }
        
        //Does the same as closure but only zero or one time
        if(p[j].equals("?"))
        {
            //Sets it to the previous value and makes the next state of the ?
            set_State(j, p[j], j, j+2);
            j++;
            return;
        }
        
        //To ensure an ArrayOutOfBoudnds Exception does not occur
        if(j+1 < p.length)
        {
            //The Set of case to check that it is not the specific character, if it is, it skips out of the output
            if(p[j].equals("^") && p[j+1].equals("["))
            {
                //If the second item was a star it increments correctly
                if(starStart)
                {
                    j++;
                }
                set_State(j, p[j], j+1, j+1);

                //Jumping 2 to get the next literal
                j += 2;
            
                //Runs the or case to get all the literals
                j = orSets(j);
                return;
            }
        
            //The or case, to check which of the items it is and chooses which state to go to
            if(p[j+1].equals("|") || p[j].equals("|"))
            {
                orStatement();
                return;
            }
            
            //All other cases are dealt with by Factors
            else
            {
                num = Factor();
            }
        }
        //All other cases are dealt with by Factors
        else
        {
            num = Factor();
        }
    }


    /*
        All remaining cases to be built, this includes (), literals and the escape character
    */
    public int Factor()
    {
        if(j < p.length)
        {
        // ( Case
        if(p[j].equals("(") || isBracket == true)
        {
            //If the last character was not a ( then don't make a new expression, instead make continue through the factors adding the new items until you reach the )
            if(isBracket == false)
            {
                set_State(j, p[j], j+2, j+2);
                
                //Gets the next character
                j++;
                
                isBracket = true;
                Expression();
            }
            
            //Ends when it gets to the closing ()
            else if(p[j].equals(")"))
            {
                set_State(j, p[j], j+2, j+2);
                j++;
            } 
        }
        if(j < p.length)
        {
            //Escape character
            if(p[j].equals("\\"))
            {
               System.out.println("ESCAPE");
               return -1;
            }
            
            //Literal Case, which is any case that is not stated in the FSM before
            else
            {
                //Checks if it is a valid length to have a next character, if it is not, it is assumed no special character cases are happening
                if(j+1 < p.length)
                {
                    //If the next value is a special case of a * or a ? it sets the state of the current item to the next value afterwards
                    if(p[j+1].equals("*") || p[j+1].equals("?"))
                    {
                        set_State(j, p[j], j+2, j+2);
                    }
                    //Otherwise proceeds as normal
                    else
                    {
                        //If the first character was a special case, it increments correctly
                        if(starStart)
                        {
                            set_State(j, p[j], j+2, j+2);
                        }
                        else
                        {
                            set_State(j, p[j], j+2, j+2);
                        }
                    }
                }
                //Otherwise proceeds as normal
                else if(j < p.length)
                {
                    //If the first character was a special case, it increments correctly
                    if(starStart)
                    {
                        set_State(j, p[j], j+2, j+2);
                    }
                    else
                    {
                        set_State(j, p[j], j+2, j+2);
                    }
                }
            }
        }
        }
        //Goes to the next indexed item
        j++;
        return j;
    }
    
    /*
        Deals with any item inside the ^[] case or the [] case
    */
    public int orSets(int count)
    {
        //Sets the state of the opening bracket
        set_State(j-1, p[j-1], j+1, j+1);
    
        //Gets the closing bracket position
        int closeBracket = count;
        int x = 0;
        String str = "";
        
        //Checks that it is not the closing bracket of the set or if it is the first character it reads it as a literal
        while(!p[closeBracket].equals("]") || x == 0)
        {
            closeBracket++;
            x++;
        }
        
        //Resets the counter to allow us to reuse it for the items in the list
        x = 0;
        closeBracket++;
            while(true)
            {
                //Counts for the case that the first character ] can be an object literal
                if(!(p[count].equals("]")) || x == 0)
                {
                    //Get next character
                    int next = count + 1;
                    set_State(count, p[count], next, closeBracket);
                    count++;
                    x++;
                }
                //If it is the end of the brackets, close it.
                else
                {
                    set_State(count, p[count], count+2, count+2);
                    count++;
                    break;
                }
            }
        return count;
    }
    
    /*
        Deals with anything in the | case and sets the states appropriately
    */
    public void orStatement()
    {
        //DECLARE VARIABLES
        int state1 = 0;
        int state2 = 0;
        int orState = j;
        boolean setState2 = true;
            
            //If it ran the case where the next character was the | it sets the increment so the states work correctly
            if(p[j+1].equals("|"))
            {
                j++;
            }
            //Checks that these have no special symbols, if there is it gets the literal from the spot before
            if(j-1 >= 0)
            {
                if(p[j-1].equals("*") || p[j-1].equals("?"))
                {
                    state1 = j-1;
                }
            }
            else
            {
                state1 = j;
            }
        
            //Sets the position or the | character
            orState = j+1;

            //Sets the next state to the symbol after the 'or'
            state2 = j+1;
            
            //Prevents ArrayOutOfBounds Exception
            if(j+2 < p.length)
            {
            //Checks if there are special cases of or or paranthesis
            if(p[j+2].equals("(") || p[j+2].equals("[") || p[j+2].equals("^"))
            {
                //Goes to the 2nd character it open that case
                j+= 2;
                setState2 = false;
            }
            //Checks for special cases such as: * or ?
            else
            {
                //Prevents ArrayOutOfBounds Exception
                if(j+3 < p.length)
                {
                    //Checking for special cases and handling correctly
                    if(p[j+3].equals("*") || p[j+3].equals("?"))
                    {
                        j+= 4;
                    }
                    //If none of those cases exist it goes to the next literal
                    else
                    {
                        //Makes it go to the next thing after the or.
                        j+=3;   
                    }
                }
                //If one will occur just increments correctly
                else
                {
                    j += 3;
                }
            }
            }
            //Other increments correctly
            else
            {
                j+=2;
            }
            //Checks an ArrayOutOfBounds Excpetion won't occur for special cases
            if(state1 - 1 >= 0)
            {
                //Checks that the current character isn't a special case, if it is, it ignores it
                if(!p[state1].equals("*") && !p[state1].equals("?"))
                {
                    //Sets the state for the literal
                    set_State(state1, p[state1], j+1, j+1);
                }
            }
            //Otherwise proceeds as normal
            else
            {
                if(state1-1 >= 0)
                {
                    set_State(state1-1, p[state1-1], j+1, j+1);
                }
                else
                {
                    set_State(state1, p[state1], j+1, j+1);
                }
            }
        
            //Checks for special cases on the current state
            if(p[state1].equals("*") || p[state1].equals("?"))
            {
                //Prevents ArrayOutOfBounds Exception
                if(state2+1 < p.length)
                {
                    //Checking for special cases
                    if(p[state2+1].equals("*"))
                    {
                        //Sets the state of the or operater
                        set_State(orState-1, "|", state1+1, state2+2);
                    }
                    else
                    {
                        //Sets the state of the or operater
                        set_State(orState-1, "|", state1+1, state2+1);
                    }
                }
                else
                {
                    //Sets the state of the or operater
                    set_State(orState-1, "|", state1+1, state2+1);
                }
            }
            //If no special case, proceeds as normal
            else
            {
                //Checks if State 2 has a special case, and this is to prevent ArrayOutOfBounds Exception
                if(state2+1 < p.length)
                {
                    if(p[state2+1].equals("*") || p[state1].equals("?"))
                    {
                        set_State(orState-1, "|", state1+1, state2+2);
                    }
                }
                //Othweise proceeds as normal
                else
                {
                    //Sets the state of the or operater
                    set_State(orState-1, "|", state1+1, state2+1);
                }
            }
            //If State 2 is to be written as not cause issues, it will go through this
            if(setState2 == true)
            {
                //Prevents ArrayOutOfBounds Exception
                if(state2 + 1 < p.length)
                {
                    //Checking for special cases
                    if(p[state2+1].equals("*") || p[state2+1].equals("?"))
                    {
                        //Sets the state of the next operator
                        set_State(state2, p[state2], state2+2, state2+2);
                    }
                }
            }
            j--;        
            return;
    }
    
    /*****************************************************/
    /*****************************************************/
    /*                                                   */
    /*            Non-general Methods                    */
    /*                                                   */
    /*****************************************************/
    /*****************************************************/
    
    /*
        Creates the states of the FSM to make sure they are in the right current state, next state and branch correctly
    */
    public void set_State(int state, String s, int firstState, int secondState)
    {
        state = state + 1;
     
        System.out.println(state + " " + s + " " + firstState + " " + secondState);
    }
}