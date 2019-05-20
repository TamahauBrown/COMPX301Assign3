class REcompiler
{
    int num = 0;
    String p [];
    int j = 0;
    int state = 0;
    boolean isBracket = false;
    public static void main(String [] args)
    {
        REcompiler re = new REcompiler();
        
        //Gets regexp pattern as an arg
        String regexp = args[0];
        re.p = new String[regexp.length()];
        for(int i = 0; i < regexp.length(); i++)
        {
            re.p[i] = Character.toString(regexp.charAt(i));
            //System.out.println(re.p[i]);
        }
        re.Expression();
    }

    public void Expression()
    {
        if(num != -1)
        {
            //Cover E --> T
            Term();
            
        //Does not accept \ as a char because it is used as a parsing //character in Java
        if(!(p[j].equals("\n")))
           {
            //Covers E --> TE
             Expression();  
           }
        }
        else
        {
            System.out.println("Exiting Parser");
            return;
        }
    }

    public void Term()
    {
        //System.out.println(num);
        if(num == -1)
        {
            return;
        }
        //Closure
        if(p[j].equals("*"))
        {
            j++;
            return;
        }
        
        //Checks to see if the next character is an or
        if(p[j+1].equals("|"))
        {
            int state1 = 0;
            int state2 = 0;
            int orState = j+1;
            boolean setState2 = true;
            System.out.println(p[j] + p[j+1] + p[j+2]);
            
            //Checks that these have no special symbols, if there is it gets the literal from the spot before
            if(p[j].equals("*") || p[j].equals("?"))
            {
                state1 = j-1;
            }
            else
            {
                state1 = j;
            }
            //Sets the next state to the symbol after the 'or'
            state2 = j+2;
            
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
                    if(p[j+3].equals("*") || p[j+3].equals("?"))
                    {
                        j+= 4;
                    }
                    //If none of those cases exist it goes to the next literal
                    else
                    {
                        //Makes it go to the next thing after the or.
                        //DOESNT WORK WITH CASES LIKE a*|(ab)
                        j+=3;   
                    }
            }
            //Checks that the current character isn't a special case, if it is, it ignores it
            if(!p[state1].equals("*") && !p[state1].equals("?"))
            {
                //Sets the state for the literal
                set_State(state1, p[state1], j, j);
            }
            
            //Sets the state of the or operater
            set_State(orState, p[orState], state1, state2);
            
            if(setState2)
            {
                //Sets the state of the next operator
                set_State(state2, p[state2], j+1, j+1);
            }
            //Goes to the factor case? Unsure if this is correct, go back to Expression???????
            Factor();
        }
        
        ///////////////////////////////////////////////////
        //WORKS
        //Start of the not included or
        if(p[j].equals("^") && p[j+1].equals("["))
        {
            System.out.println("HI");
            System.out.println(p[j] + p[j+1]);
            //Jumping 2 to get the next literal
            j += 2;
            
            //Runs the or case to get all the literals
            j = orSets(j);
            
            //TODO: Make it do something with that case
        }
        
        //End of not included or
        
        //WORKS
        //Deals with the or case
        if(p[j].equals("["))
        {
            j++;
            j = orSets(j);
        }
        
        //End of the set or case
        
        ////////////////////////////////////////////////////////
        // ? case, got it working with one time 
        //TODO: add 0 times case
        if(p[j].equals("?"))
        {
            //Makes it run one time
            for(int i = 0; i < 1; i++)
            {
                System.out.println(p[j-1]);
                j++;
            }
        }
        else
        {
            //All terms have a factor
            num = Factor();
        }
        return;
    }

    public int Factor()
    {
        num = 0;
        //TODO: Create isSyntax(String) NEED HELP AS DONT UNDERSTAND
        if(isSyntax(p[j]))
        {
            //If it is []
            set_State(state, p[j], state + 1, state + 1);
            j++;
            num = state;
            state++;
            return num;
        }
        // ( Case WORKS
        if(p[j].equals("(") || isBracket == true)
        {
            //If the last character was not a ( then don't make a new expression, instead make continue through the factors adding the new items until you reach the )
            if(isBracket == false)
            {
                set_State(j, p[j], j+1, j+1);
                //Gets the next character
                j++;
                
                isBracket = true;
                Expression();
            }
            
            // ) Case WORKS
            else if(p[j].equals(")"))
            {
                set_State(j, p[j], j+1, j+1);
                j++;
            } 
        }
        
        //Escape character WORKS
        if(p[j].equals("\\"))
        {
               System.out.println("ESCAPE");
               return -1;
        }
        //Literal Case WORKS
        else
        {
            System.out.println("LITERAL " + p[j]);
            set_State(j, p[j], j+1, j+1);
        }
        j++;
        return num;
    }
    
    public int orSets(int count)
    {
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
                    System.out.println(str);
                    count++;
                    break;
                }
            }
        return count;
    }

    public void Parse()
    {
        
    }
    
    /*****************************************************/
    /*****************************************************/
    /*                                                   */
    /*            Non-general Methods                    */
    /*                                                   */
    /*****************************************************/
    /*****************************************************/
    public void set_State(int state, String s, int firstState, int secondState)
    {
        //p[j+1] is the next state and if it is or then p[j+2]???
        System.out.println("Current State " + state + " Character " + s + " Next State 1: " + firstState + " Next State 2: " + secondState);
    }
    
    public boolean isSyntax(String s)
    {
        return false;
    }
    
}