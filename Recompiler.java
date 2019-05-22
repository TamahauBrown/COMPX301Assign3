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
        re.p = new String[regexp.length()];
        
        for(int i = 0; i < regexp.length(); i++)
        {
            re.p[i] = Character.toString(regexp.charAt(i));
        }
        re.Expression();
    }

    public void Expression()
    {
        if(firstTime)
        {
            if(p[j+1].equals("*") || p[j+1].equals("?") || p[j+1].equals("|"))
            {
                set_State(-1, ".", j+2, j+2);
                starStart = true;
            }
            else
            {
                set_State(-1, ".", j+1, j+1);
            }
            firstTime = false;
        }
        if(num != -1)
        {
            //Cover E --> T
            Term();
            
            if(j > p.length - 1)
            {
                System.out.println("EXIT");
                return;
            }
            
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
        if(num == -1)
        {
            return;
        }
        
        //Closure
        if(p[j].equals("*"))
        {
            
            //Covers the * case
            set_State(j, p[j], j, j+2);
            j++;
            return;
        }
                
        //End of not included or
        
        //Deals with the or case
        if(p[j].equals("["))
        {
            j++;
            j = orSets(j);
            return;
        }
        
        //End of the set or case
        
        // ? Case
        if(p[j].equals("?"))
        {
            //Sets it to the previous value and makes the next state of the ?
            set_State(j, p[j], j-1, j-1);
            j++;
            return;
        }
        
        if(j+1 < p.length)
        {
            if(p[j].equals("^") && p[j+1].equals("["))
            {
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
        
            //Checks to see if the next character is an or
            if(p[j+1].equals("|") || p[j].equals("|"))
            {
                orStatement();
                return;
            }
            else
            {
                num = Factor();
            }
        }
        else
        {
            num = Factor();
        }
    }


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
            
            // ) Case WORKS
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
            //Literal Case
            else
            {
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
                        if(starStart)
                        {
                            set_State(j, p[j], j+2, j+2);
                        }
                        else
                        {
                            set_State(j, p[j], j+1, j+1);
                        }
                    }
                }
                //Otherwise proceeds as normal
                else if(j < p.length)
                {
                    if(starStart)
                    {
                        set_State(j, p[j], j+2, j+2);
                    }
                    else
                    {
                        set_State(j, p[j], j+1, j+1);
                    }
                }
            }
        }
        }
        //Goes to the next indexed item
        j++;
        return j;
    }
    
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
                    set_State(count, p[count], count+1, count+1);
                    count++;
                    break;
                }
            }
        return count;
    }
            
    public void orStatement()
    {
        int state1 = 0;
            int state2 = 0;
            int orState = j;
            boolean setState2 = true;
            
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
            orState = j+1;

            //Sets the next state to the symbol after the 'or'
            state2 = j+1;
            
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
                //System.out.println("HERE " + p[j+3]);
                if(j+3 < p.length)
                {
                    
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
                else
                {
                    j += 3;
                }
            }
            }
            else
            {
                j+=2;
            }
            if(state1 - 1 >= 0)
            {
                //Checks that the current character isn't a special case, if it is, it ignores it
                if(!p[state1].equals("*") && !p[state1].equals("?"))
                {
                    //Sets the state for the literal
                    set_State(state1, p[state1], j+1, j+1);
                }
            }
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
            if(p[state1].equals("*") || p[state1].equals("?"))
            {
                if(state2+1 < p.length)
                {
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
            else
            {
                if(state2+1 < p.length)
                {
                    if(p[state2+1].equals("*") || p[state1].equals("?"))
                    {
                        set_State(orState-1, "|", state1+1, state2+2);
                    }
                }
                else
                {
                    //Sets the state of the or operater
                    set_State(orState-1, "|", state1+1, state2+1);
                }
            }
        if(setState2 == true)
        {
            if(state2 + 1 < p.length)
            {
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
    public void set_State(int state, String s, int firstState, int secondState)
    {
        state = state + 1;
     
        System.out.println(state + " " + s + " " + firstState + " " + secondState);
    }
    
}