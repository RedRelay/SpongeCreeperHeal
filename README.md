# SpongeCreeperHeal

SpongeCreeperHeal is a *Work In Progress* project, source are now available but all feature are not implemented.
Currently SpongeCreeperHeal is not suitable to be deploy on real servers.
SpongeCreeperHeal is not only a porting of [ForgeCreeperHeal](https://github.com/RedRelay/ForgeCreeperHeal) from Forge to Sponge.

It is a complete code refactoring to make it easier to understand and to contribute.
SpongeCreeperHeal started 18th july 2018, at the same date of Minecraft 1.13 !

With a lot of new skill in development, I will put the best of I could in this plugin.

# Development is currently PAUSED

Sponge offer me the possibility to make a plugin like bukkit does before license issues.
SpongeCreeperHeal is a server side mod, so I would like vanilla version could join a server with my mod.
Sponge can handle it. Moreover Sponge promise one more thing : "Most plugins developed with the Sponge API should work across several different versions of Minecraft without needing to be updated.". That's quite true, because by using Sponge we have to use Sponge API instead of Minecraft API. Sponge API then call Minecraft API itself meaning all change to Minecraft API accross versions are done into Sponge API. However for being meaningfull Sponge API have to be updated when Minecraft API evolve.
Since I wrote theses lines, Minecraft is 1.16 and Sponge API is still 1.12 ... 3 years after Minecraft 1.12 was released ...
I have paused the SpongeCreeperHeal development because who still play 1.12 ? 

## When the code and the Sponge plugin will be available ?

I need to create a strong functionnal base before publishing the code.
Here is a base roadmap :

- [x] Implement a per chunk / per explosion blockSnapshots stucture
- [x] Handle tick and consume healable objects
- [x] Restore a blockSnapshot
- [x] Persist data
- [x] Create a dependency engine
- [x] Register all dependencies -> the longest step (tracker : https://github.com/RedRelay/SpongeCreeperHeal/issues/4)
- [ ] Support multi blocks
- [ ] Configurable healing clock

When all theses task will be done, the code will be published.
I've planned additionnal develpments :

- [ ] Configurable filters on explosion sources
- [ ] Healing commands with permissions
- [ ] Simulate block healing when chunk is not loaded

Stay tuned :)

## I want help to get a release faster !

You are welcome !
Just notify me by starring the project :)
Pull request is open !

Here is the dependency tracker : https://github.com/RedRelay/SpongeCreeperHeal/issues/4
