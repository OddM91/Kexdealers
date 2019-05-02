package encodeCommands;

import java.io.DataOutputStream;
import java.io.IOException;

import ecs.AbstractComponent;

public interface EncoderCommand {
	
	public void encode(DataOutputStream stream, AbstractComponent comp) throws IOException;
}
