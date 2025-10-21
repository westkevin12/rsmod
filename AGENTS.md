# AGENT.md

## Introduction to RS Mod

RS Mod is a RuneScape game-server emulator designed from the ground up to be a complete, maintainable, and mechanically accurate development framework. It prioritizes a clean, scalable architecture to facilitate high-quality content development.

The project is built using **Java 21** and **Kotlin**, and it relies on **Gradle** for dependency management and build automation.

-----

## 🏛️ Core Architecture

The project is a multi-module Gradle project, with a strong separation of concerns between its main components. This design makes the codebase easier to navigate, maintain, and scale.

  * **`engine` 🧠:** This is the server's foundation. It contains the low-level, game-agnostic logic for systems like pathfinding, the main game loop, and event handling. The engine provides the core rules but does not define game-specific content.

  * **`api` 🛠️:** This is the developer's primary toolbox. The API exposes engine features through a clean, accessible layer, simplifying content development. When creating skills or other game features, you'll interact with `api` modules for things like script events (`onOpLoc1`), entity management (`NpcRepository`, `ObjRepository`), and player functions (`statAdvance`, `mes`).

  * **`content` 🎮:** This is where all game-specific features are implemented. Skills (e.g., Woodcutting, Mining), minigames, quests, and area-specific logic are all located here. These content modules are built using the tools provided by the `api` modules.

  * **`server` 🚀:** This is the final assembly point. The `server/app` module bootstraps the application, loading all the engine, API, and content modules to launch the live game server.

-----

## 💾 Data Persistence and Cache

### Player Data Persistence

Player data, including accounts, stats, inventory, and more, is persisted through a database. The default implementation uses **SQLite**, and the project incorporates **Flyway** to manage database schema migrations automatically.

The `.data` directory does **not** store player data. Instead, it holds generated **symbol files (`.sym`)** that map human-readable names like `objs.bronze_axe` to their numerical in-game IDs, which vastly improves the development experience.

### Cache Handling: The "Enricher"

One of the project's most powerful features is its automated cache handling system, managed by the **`api/cache-enricher`** module. This system allows for data-driven development where the game cache is modified programmatically.

The process is as follows:

1.  **Fetch Vanilla Cache:** A clean, unmodified OSRS cache is downloaded on first setup.
2.  **Define Enrichments:** Developers define custom game data (e.g., new item parameters) in human-readable configuration files (like `locs.toml` and `objs.toml`).
3.  **Pack Enriched Cache:** During the build process, the `cache-enricher` reads these configs and packs a new, custom version of the cache.
4.  **Run Server:** The server uses this newly packed cache.

This approach keeps all custom modifications version-controlled, easy to track, and simple to maintain.

-----
