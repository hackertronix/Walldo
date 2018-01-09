package io.execube.monotype.walldo.utils;

import java.util.Random;

/**
 * Created by hackertronix on 18/03/17.
 */

public class Utils {

    public static final String API_ENDPOINT= "http://unsplash.it";

    public static final String WIDTH="500";
    public static final String HEIGHT="750";

    public static final String ACTION_DATABASE_UPDATED = "com.example.hackertronix.firebaseauthtest.utils.ACTION_DATA_UPDATED";



    public static final String LIST_IMAGE_ENDPOINT="http://unsplash.it/500/750?image=";
    public static final String WIDGET_IMAGE_ENDPOINT="http://unsplash.it/50/50?image=";
    public static final String FULL_RES_IMAGE_ENDPOINT="http://unsplash.it/";


    public static final String[] PROGRESS_DIALOG_MESSAGES= {
            "Contacting The Empire.....",
            "Turning on the Arc Reactor....",
            "Charging the Flux Capacitor....",
            "Fastening seatbelts....",
            "Setting phasers to stun....",
            "Shovelling coal into the servers....",
            "Sending your data to the NSA....",
            "Setting font to Comic Sans....",
            "Launching escape pods....",
            "Warming up the Death Star....",
            "Cleaning the Light Sabers...",
            "Doing something awesome...."
    };


    public static int[] getRandomInts()
    {
        int max_range = 1085;
        int randomInts[] = new int[100];

        Random random = new Random();

        for (int i =0 ; i <100; i++)
        {
            int num = random.nextInt(max_range);
            if(num == 0)
            {
                num = doAgain(num);
            }

            else {
                randomInts[i]=num;
            }
        }

        return randomInts;

    }

    private static int doAgain( int num) {

        Random random = new Random();

        num = random.nextInt(1085);

        while(num==0)
        {
            doAgain(num);
        }

        return num;
    }


}
