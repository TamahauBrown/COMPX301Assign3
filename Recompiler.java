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
            System.out.println(p[j] + p[j+1] + p[j+2]);
            j+=3;
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
            /*
            // * ASSUMPTION *
            //If the user adds another paranthesis inside the paranthesis it closes it with an automatic closing bracket 
            if(p[j].equals("(") && isBracket == true)
            {
                System.out.println("Close bracket");
                isBracket = false;
            }
            */
            //If the last character was not a ( then don't make a new expression, instead make continue through the factors adding the new items until you reach the )
            if(isBracket == false)
            {
                //Gets the next character
                j++;
                
                isBracket = true;
                Expression();
            }
            
            // ) Case WORKS
            else if(p[j].equals(")"))
            {
                System.out.println("Close bracket");
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
                    //p[count]
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