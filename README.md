# Brief Description 
We are a member of the coast guard force in charge of a rescue boat that goes
into the sea to rescue other sinking ships. The area in the sea that we can navigate
is an (𝑚 × 𝑛) grid of cells where 5 ≤ 𝑚; 𝑛 ≤ 15. Every action we take will have
a significant impact on the other ships, therefore we’ll need to develop a search
algorithm that will plan the route to rescue the trapped passengers. Several
ships are scattered at random locations, and each has a random initial number of
passengers 𝑝, where 0 < 𝑝 ≤ 100. Every action taken will, unfortunately, result
in the death of a person from each trapped ship. The ship will become a wreck
after all of its crew members die, but our mission does not end there; we must still
retrieve the ship’s black box. The black box has exactly 20 time step before the
entire wreck sinks, therefore, it’s critical to retrieve it to prevent adverse incidents
in the future. Unfortunately, we can’t always save everyone because our boat has
a limited capacity 𝑐 where 30 <= 𝑐 < 100, so we must drop carried-on passengers
at several known rescue stations before returning for the others. Several crucial
decisions must be made to save as many passengers as possible and recover as
many black boxes as possible. Many search algorithms, including BFS, DFS,
IDS, UC, GR, and AS, have been investigated in order to find the optimal route
for the coast guard boat, which will, hopefully, result in fewer deaths and lost
black boxes.

# Actions
* Pick-up: The coast guard picks up as many passengers off a ship as its remaining
capacity allows. This can be done on a ship that is in the same cell as the coast
guard and it only affects this ship. Once a passenger is picked up by the coast
guard, they will not expire and will stay on the coast guard boat until they are
dropped at a station.
* Drop: The coast guard drops all passengers it is currently carrying at a station.
This can only be done when the coast guard and the station are in the same cell
and it resets the remaining capacity of the coast guard boat to 0.
* Retrieve: The coast guard boat retrieves a black box. This can only be done when
the coast guard boat is in the same cell as a wreck with a black box which has
not been completely damaged yet. This action does not affect the coast guard’s
remaining capacity at all.
* Movement in any of the 4 directions (up, down, left, right) within the grid
boundaries

# The implemented Algorithms

I implemented a class for each strategy. So, we have six different classes for the
required strategies :
* a) Breadth-first search.
* b) Depth-first search.
* c) Iterative deepening search.
* d) Greedy search with at least two heuristics.
* e) A∗ search with at least two admissible heuristics. A trivial heuristic (e.g.h(n) = 1)
* is not acceptable.

# For more details, please refer to the report.
