package Structure.Interfaces;

import java.util.Collection;

public interface TransferRenderer<Q extends AbstractQuestion,C extends ClientInstructor> {
	/*
	 *Transfers the control from one client to another
	 *Arguments:
	 *client - The client that has answered the question
	 *answer - the answer that has been received
	 *return - true if the round is over
	 */
	public boolean transfer(C client,String answer);
	
	/*
	 *initiate the transfer rendering
	 *questions - All the questions
	 *answers - All the answers to be present in the same order
	 *clients - All the clients that are logged in . 
	 *			It must be ensured that this array remains unchnaged as a reference is stored by the class
	 */
	public void init(Collection<Q> questions,Collection<Structure.Answer> answers,C[] clients);
}