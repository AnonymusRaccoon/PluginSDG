package moe.sdg.PluginSDG.exceptions;

public class MapNotFoundException extends MapException
{
	@Override
	public String toString()
	{
		return "The specified map couldn't be found.";
	}
}
