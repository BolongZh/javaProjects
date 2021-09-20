package gh2;

// TODO: uncomment the following import once you're ready to start this portion
// import deque.Deque;
// TODO: maybe more imports

import deque.ArrayDeque;
import deque.LinkedListDeque;

//Note: This file will not compile until you complete the Deque implementations
public class GuitarString {
    /** Constants. Do not change. In case you're curious, the keyword final
     * means the values cannot be changed at runtime. We'll discuss this and
     * other topics in lecture on Friday. */
    private static final int SR = 44100;      // Sampling Rate
    private static final double DECAY = .996; // energy decay factor

    /* Buffer for storing sound data. */
    private LinkedListDeque<Double> buffer;

    /* Create a guitar string of the given frequency.  */
    public GuitarString(double frequency) {
        double temp = Math.round(SR/frequency);
        int capacity = (int) temp;
        buffer = new LinkedListDeque<Double>();
        while (buffer.size()<capacity) {
            buffer.addFirst(0.0);
        }
    }

    /* Pluck the guitar string by replacing the buffer with white noise. */
    public void pluck() {
        for (int i = buffer.size();i>0;i--){
            buffer.removeFirst();
            buffer.addLast(Math.random()-0.5);
        }

        // TODO: Dequeue everything in buffer, and replace with random numbers
        //       between -0.5 and 0.5. You can get such a number by using:
        //       double r = Math.random() - 0.5;
        //
        //       Make sure that your random numbers are different from each
        //       other. This does not mean that you need to check that the numbers
        //       are different from each other. It means you should repeatedly call
        //       Math.random() - 0.5 to generate new random numbers for each array index.
    }

    /* Advance the simulation one time step by performing one iteration of
     * the Karplus-Strong algorithm.
     */
    public void tic() {
        // TODO: Dequeue the front sample and enqueue a new sample that is
        //       the average of the two multiplied by the DECAY factor.
        //       **Do not call StdAudio.play().**
        double toAdd = sample();
        buffer.removeFirst();
        buffer.addLast(toAdd);
    }

    /* Return the double at the front of the buffer. */
    public double sample() {
        // TODO: Return the correct thing.
        return 0.5*DECAY*(buffer.get(0)+buffer.get(1));
    }
}
    // TODO: Remove all comments that say TODO when you're done.
