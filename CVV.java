package ttools;

import java.util.ArrayList;
import java.io.*;

public class CVV
{
	public CVV(int length, double threshold) {
        this.length = length;
        this.threshold = threshold;
        dp0 = new double[length];
        price0 = new ArrayList<Double>();
        cv0 = new ArrayList<Double>();	
        //sigCCI0 = new ArrayList<Integer>();
    }

	public int calcCVV(double price) {
        //populate initial value and validate price
        int size = price0.size();
        if (size < length-1)
        {
            price0.add(price);
            cv0.add(0.0);
            //sigCCI0.add(0);
            return 0;
        }

        double lastpx = price0.get(size-1);
        //if (lastpx == price || Math.abs(lastpx - price) >= 0.06) return 0; //filter, move to higher level

        price0.add(price);
        size = price0.size();

        //calc ma
        double sum = 0.0;
        for(int i=size-length; i < size; ++i)
            sum += price0.get(i);
        double ma = sum/length;

	   //calc ma0
        double ma0 = 0;
        sum = 0.0;
        if (size < (length + 2))
            { 
                 ma0 = price0.get(0);
            }
        else if (size < (2*length))
            {
                 int length0 = size - length;
                 for (int i=0; i<length0; ++i)
                 {
                     sum += price0.get(i);
                 }
                 ma0 = sum/length0;
            }
        else 
            {
                 for (int i=0; i<length; ++i)
                 {
                     int idx = size - 2*length + i;
                     sum += price0.get(idx);
                 }
                 ma0 = sum/length;
            }

        //calc dp0
        for(int i=0; i<length; ++i)
            dp0[i]=Math.pow((price0.get(size-length+i)-ma),2);

        //calc std
        sum = 0.0;
        for(int i=0; i<length; ++i)
            sum += dp0[i];
        double s0 = Math.sqrt(sum/length);

        //get sign (1, or -1)
	   int sign = 0;
        if ( (ma-ma0) >= 0) 
            {
                sign = 1;
            }
        else
            {
                sign = -1;
            }
		
        //calc cv
        double cv = sign*1000*s0/ma;

        //calc sigcv
        int sigcv = 0;
        double lastcv = cv0.get(cv0.size() - 1);
//         if (cci < threshold && lastcci >= threshold) 
//             sigcci = 1;
//         else if (cci > -threshold && lastcci <= -threshold) 
//             sigcci = -1;
        if (cv >= threshold) 
            sigcv = 1;
        else if (cv <= -threshold) 
            sigcv = -1;

        cv0.add(cv);
        //sigCCI0.add(sigcci);
        return sigcv;
    }

    public int getCVVSig() {
        int sigcv = 0;
        double cv = cv0.get(cv0.size() - 1);
        if (cv >= threshold) 
            sigcv = 1;
        else if (cv <= -threshold) 
            sigcv = -1;

        return sigcv;
    }
    
/*	public double calcCCInew(double price) {
        //populate initial value and validate price
        int size = price0.size();
        if (size <= length)
        {
            price0.add(price);
            cci0.add(0.0);
            //sigCCI0.add(0);
            return 0;
        }

        double lastpx = price0.get(size-1);
        //if (lastpx == price || Math.abs(lastpx - price) >= 0.08) return 0; //filter, move to higher level

        price0.add(price);
        size = price0.size();

        //calc ma
        double sum = 0.0;
        for(int i=size-length; i < size; ++i)
            sum += price0.get(i);
        double ma = sum/length;

        //calc dp0
        for(int i=0; i<length; ++i)
            dp0[i] = Math.abs(price0.get(size-length+i) - ma);

        //calc s0
        sum = 0.0;
        for(int i=0; i<length; ++i)
            sum += dp0[i];
        double s0 = sum/length;
		
        //calc cci
        double cci = (price-ma)/0.015/s0;

        cci0.add(cci);

        return cci;
    }     */

	int length;
	double threshold;
	double[] dp0;
	ArrayList<Double> price0;
	ArrayList<Double> cv0;
	//ArrayList<Integer> sigCCI0; //not necessary as a collection

// 	public static void main(String[] args) { 
// 		System.out.println("Testing CCI");
// 		CCI cci = new CCI(10, 5.0);

//         BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
//         try {
//             while(true) {
//                 if (!in.ready()) break;
//                 double price = Double.parseDouble(in.readLine());
//                 cci.calc_cci(price);
//                 int size = cci.price0.size();
//                 System.out.format("%f, %f, %d\n", cci.price0.get(size-1), cci.cci0.get(size-1), cci.sigCCI0.get(size-1));
//             }
//         } catch (IOException e) {
//             System.out.println(e + ", failed reading in double");
//         }
// 	}
}
