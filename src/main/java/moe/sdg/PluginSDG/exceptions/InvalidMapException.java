package moe.sdg.PluginSDG.exceptions;

public class InvalidMapException extends MapException
{
	@Override
	public String toString()
	{
		return "The specified map is invalid or corrupted and couldn't be loaded.";
	}
}
