class REcompiler
{
    public static void main(String [] args)
    {
        String regexp = args[0];
    }

    public void Expression()
    {
        //Cover E --> T
        Term();
        //Does not accept \ as a char because it is used as a parsing //character in Java
        if(p[j] != '\n')
           {
            //Covers E --> TE
             Expression();  
           }
    }

    public void Term()
    {
        //All terms have a factor
        Factor();
        
        //Closure
        if(p[j] == "*")
        {
            j++;
            return;
        }
        
        //Single or
        if(p[j] == "|")
        {
            j++;
            Factor();
        }
        //
        
        //Deals with the or case
        if(p[j] == "[")
        {
            j++;
            orSets(j);
        }
        
        //End of the set or case
        
        //Start of the not included or
        if(p[j] == '^' && p[j+1] == "[")
        {
            //Jumping 2 to get the next literal
            j += 2;
            
            //Runs the or case to get all the literals
            orSets(j);
            
            //TODO: Make it do something with that case
        }
        
        //End of not included or
        
        if(p[j] == '?')
        {
            //Make it run similar to closure.
        }
        return;
    }

    public int Factor()
    {
        int num;
        //TODO: Create isSyntax(byte)
        if(isSyntax(p[j]))
        {
            //TODO: Create set_State()
            set_State(state, p[j], state + 1, state + 1);
            j++;
            num = state;
            state++;
            return num;
        }
        if(p[j] == '(')
        {
            j++;
            num = Expression();
            if(p[j] == ')')
            {
                j++;
            }
            else
            {
                System.out.println("Illegal expression detected");
            }
        }
        return num;
    }
    
    public int orSets(int count)
    {
            count++;
            while(true)
            {
                //Counts for the case that the first character ] can be an object literal
                if(p[count] != "]" && count != 0)
                {
                    //Get next character
                    String str = getCh();
                    count++;
                }
                //If it is the end of the brackets, close it.
                else
                {
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
    
    
}