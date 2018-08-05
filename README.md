# SpongeCreeperHeal

SpongeCreeperHeal is a *Work In Progress* project, source are not available yet due to current unstabilities.
SpongeCreeperHeal is not only a porting of [ForgeCreeperHeal](https://github.com/RedRelay/ForgeCreeperHeal) from Forge to Sponge.
It is a complete code refactoring to make it easier to understand and to contribute.
SpongeCreeperHeal started 18 jully 2018, at the same date of Minecraft 1.13 ! (Was completely uncalculated)

With a lot of new skill in develpment, I will put the best of I could in this plugin.

I don't use Java since 2017, I'm now a professionnal JavaScript devlopper for a leader e-commerce compagny in France.
As Minecraft is java coded, I will use Java with some functionnal programming I've learned from JS. E.g -> Lambda !

## When the code and the Sponge plugin will be available ?

I need to create a strong functionnal base before publishing the code.
Here is a base roadmap :

- [x] Implement a per chunk / per explosion blockSnapshots stucture
- [x] Handle tick and consume healable objects
- [x] Restore a blockSnapshot
- [x] Persist data
- [ ] Create a dependency engine and sort blocks -> the hardest & longest step
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

Once I've done the dependency engine, if at least one developper is interessed by helping me in alpha stage, I will post the source and a "Dependency Tracker" on github. This tracker will determinate what is done and what should be done before releasing.

Dependencies consist to tell my engine, which condition require block to be placed.
I didn't play Minecraft since 1.10, so all new changes are currently unknown for me.

Once the alpha is done and plugin release is available, pull request still remain open to everyone wanted to contribute ;)
