package decodeCommands;

import java.io.DataInputStream;
import java.io.IOException;

import ecs.AbstractComponent;

public interface DecoderCommand {
	
	public AbstractComponent decode(DataInputStream stream) throws IOException;
}
