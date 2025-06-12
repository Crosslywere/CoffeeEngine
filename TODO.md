# Goals - Viewer suggested / My ideas

- Implement rigged and dynamic meshes
- Implement shadows / lighting - Lights are entities
- Implement materials(Use PBR) - components
- Implement textures(2D and 3D) - components
- Implement default shader for entities(Use PBR)
- Implement font rendering
- Check out how to start new processes alongside the current running one

----
# Real TODOs

- Implement reading values from framebuffers

----
# Done
1. Check out Artemis (https://thelinuxlich.github.io/artemis_CSharp/); Is not a good fit for this project at the current time.
2. Sort out inheritance of Components VS SubComponents -Everything is now a component
3. Implement a framebuffer builder

----
[//]: # (Enhance this piece of Java)
[//]: # (```)
[//]: # (public final <Comp extends Component> Optional<Comp> getComponent&#40;Class<Comp> compType&#41; {)
[//]: # (    if &#40;components.containsKey&#40;compType&#41;&#41;)
[//]: # (        return Optional.of&#40;&#40;Comp&#41; components.get&#40;compType&#41;&#41;;)
[//]: # (    return Optional.empty&#40;&#41;;)
[//]: # (})
[//]: # (```)
