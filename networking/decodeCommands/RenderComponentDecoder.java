package decodeCommands;

import java.io.DataInputStream;
import java.io.IOException;

import ecs.AbstractComponent;
import ecs.Renderable;

public class RenderComponentDecoder implements DecoderCommand{

	@Override
	public AbstractComponent decode(DataInputStream stream) throws IOException{
		Renderable comp = new Renderable(0);
		
		StringBuilder assetName = new StringBuilder();
		char next;
		while((next = stream.readChar()) != '\n') {
			assetName.append(next);
		}
		comp.setResourceName(assetName.toString());
		
		return comp;
	}

}
