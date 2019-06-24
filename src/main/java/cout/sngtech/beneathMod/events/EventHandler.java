package cout.sngtech.beneathMod.events;

import cout.sngtech.beneathMod.init.ItemInit;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

@Mod.EventBusSubscriber
public class EventHandler 
{
	@SubscribeEvent
	public static void onBlockHarvestEvent(BlockEvent.HarvestDropsEvent e)
	{
		if(e.getState().getBlock() == Blocks.STONE)
		{
			e.getDrops().remove(0);
			e.getDrops().add(new ItemStack(ItemInit.ROCK));
		}
		
		else if(e.getState().getBlock() == Blocks.IRON_ORE)
		{
			e.getDrops().remove(0);
			e.getDrops().add(new ItemStack(ItemInit.IRON_ORE_ROCK));
		}
	}
	
	/*@SubscribeEvent
	public static void onPlayerEnterVoidEvent(PlayerTickEvent e)
	{
		EntityPlayer player = e.player;
		if(player.posY <= -2)
		{
			//To be removed when layer two is created
			player.sendMessage(new TextC("TODO: TRANSPORT PLAYER TO LAYER 2 OF THE WORLD!!!"));
		}
	}*/
}
