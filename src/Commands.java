


/**
 * List of all the commands necessary to play noughts and crosses including mostly menu options
 * @author Oliver
 *
 */
public class Commands 
{
	public static final String 	EXIT = "/exit",			// <>						disconnects client (TODO if in game then from that, else close entirely)
								PLAY = "/play",			// <nickname>				challenges player to game
								ACCEPT = "/accept",		// <nickname>				accept current challenge
								DECLINE = "/decline", 	// <nickname>				decline current challenge
								//SCORES = "/scores",		// <>					displays wins & losses of players
								TELL = "/tell",			// <nickname> <message>		chat to a player
								MOVE = "/move",			// <x> <y>					e.g. move 0 0 will place "counter" in top left
								LIST = "/list",			// <>						lists all online clients
								//REQUESTS = "/requests",	// <>					lists all the players who have sent you requests
								GAME = "/game",			// <opponent> <turn>		(Server sent) initiates a game telling the client who they are playing and whether they start
								LEAVE = "/leave",		// <>						leaves current game
								ERROR = "/error";		// <error message>			(Server sent) to be shown as dialog boxes in the GUI
}
