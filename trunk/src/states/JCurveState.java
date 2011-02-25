package states;

import main.client.CurveClient;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.esotericsoftware.kryonet.Client;

public abstract class JCurveState extends BasicGameState{
	private int id;
	
	public JCurveState(int id){
		this.id = id;
	}

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		// TODO Auto-generated method stub
		
	}
	
	public Client getClient(){
		return CurveClient.getInstance().getClient();
	}

	@Override
	public int getID() {
		return id;
	}

}
