# UMWA Mod - Technical Documentation

## Architecture Overview

The Ultimate Minecraft World Analyser (UMWA) mod is built with a modular architecture that separates concerns into distinct systems:

### Core Systems

#### 1. MiningDataManager (`com.umwa.core.MiningDataManager`)
- **Purpose**: Central data collection and mining statistics
- **Features**:
  - Real-time block mining tracking
  - Ore discovery logging
  - Mining efficiency calculations
  - Ore vein detection and clustering
  - Session-based statistics

#### 2. WorldAnalyzer (`com.umwa.core.WorldAnalyzer`)
- **Purpose**: World exploration and cave system analysis
- **Features**:
  - Cave system detection and mapping
  - Hotspot identification for valuable areas
  - Optimal mining path calculation
  - Branch mining pattern generation
  - Area analysis with async processing

#### 3. AIOrePrediction (`com.umwa.core.AIOrePrediction`)
- **Purpose**: Machine learning-based ore prediction
- **Features**:
  - Y-level based ore probability calculations
  - Proximity-based clustering analysis
  - Biome-aware ore generation
  - Learning from discovered ore patterns
  - Mining spot recommendations

#### 4. PerformanceAnalyzer (`com.umwa.core.PerformanceAnalyzer`)
- **Purpose**: Player performance tracking and optimization
- **Features**:
  - Mining speed analysis
  - Efficiency scoring (A+ to D grades)
  - Personalized optimization suggestions
  - Chunk-by-chunk performance tracking
  - Time-based productivity analysis

#### 5. WaypointManager (`com.umwa.core.WaypointManager`)
- **Purpose**: Location marking and navigation system
- **Features**:
  - 12 different waypoint types
  - Proximity alerts
  - Automatic waypoint creation
  - Navigation assistance
  - Waypoint aging and cleanup

### Data Structures

#### MiningSession (`com.umwa.data.MiningSession`)
- Tracks current mining session statistics
- Calculates real-time efficiency metrics
- Monitors blocks per minute rates

#### OreVein (`com.umwa.data.OreVein`)
- Represents connected ore deposits
- Calculates vein density and size
- Tracks completion progress

#### CaveSystem (`com.umwa.data.CaveSystem`)
- Maps underground cave networks
- Identifies entrances and structure
- Calculates cave volume and complexity

#### HotSpot (`com.umwa.data.HotSpot`)
- High-value mining areas
- Value scoring based on ore types
- Density calculations

#### Waypoint (`com.umwa.data.Waypoint`)
- Flexible location marking system
- Type-based categorization
- Age tracking and management

### User Interface

#### MiningHUD (`com.umwa.client.gui.MiningHUD`)
- Real-time statistics overlay
- Mining efficiency display
- Y-level recommendations
- Performance metrics

#### MinimapRenderer (`com.umwa.client.gui.MinimapRenderer`)
- Cave system visualization
- Hotspot marking
- Player position tracking
- Compass navigation

### Integration Systems

#### Mixins
- **WorldMixin**: Hooks into block state changes
- **ClientPlayerEntityMixin**: Input handling
- **MinecraftClientMixin**: Client-side processing
- **PlayerManagerMixin**: Player connection management

#### Network System
- Client-server data synchronization
- Mining data transmission
- Cave system sharing
- Performance metrics sync

## Key Features Implementation

### 🏔️ Core Mining Analysis
- **3D Cave Visualization**: `CaveSystem` class with volume calculations
- **Ore Distribution**: `HotSpot` detection with value scoring
- **Mining Efficiency**: Real-time calculation in `MiningSession`
- **Unexplored Detection**: Chunk-based exploration tracking
- **Cave Mapping**: Connected system detection via proximity
- **Depth Analysis**: Y-level based statistics

### 📊 Resource Analytics
- **AI Ore Prediction**: Machine learning in `AIOrePrediction`
- **Progress Tracking**: Percentage calculations in `WorldData`
- **Yield Reports**: Per-chunk ore statistics
- **Hotspot Detection**: Multi-criteria hotspot ranking
- **Biome Analysis**: Biome-aware ore generation patterns
- **Vein Tracking**: Complete vein following system

### 🌍 World Integration
- **Real-time Updates**: Mixin-based block tracking
- **Chunk Detection**: Loading state monitoring
- **Position Tracking**: Continuous player location
- **Multi-dimensional**: Overworld/Nether/End support
- **Structure Detection**: Automatic waypoint creation
- **Biome Recognition**: Context-aware analysis

### 🎯 Smart Mining Features
- **Optimal Paths**: A* pathfinding implementation
- **Branch Mining**: Automated pattern generation
- **Risk Assessment**: Hazard detection (lava/water/mobs)
- **Tool Recommendations**: Context-based suggestions
- **Time Estimation**: Performance-based calculations
- **Resource Prioritization**: Value-based targeting

### 🖥️ User Interface
- **Minimap**: Real-time cave and ore overlay
- **HUD Integration**: Non-intrusive statistics display
- **Waypoint System**: 12 different marker types
- **Progress Visualization**: Real-time progress bars
- **Alert System**: Proximity and discovery notifications
- **Quick Access**: Instant metric display

## Performance Considerations

### Memory Usage
- Efficient data structures with WeakHashMap where appropriate
- Chunk-based data organization
- Automatic cleanup of old data

### CPU Performance
- Asynchronous analysis processing
- Configurable analysis radius
- Optimized pathfinding algorithms
- Smart caching of expensive calculations

### Network Efficiency
- Minimal packet sizes
- Client-side prediction
- Batch data transmission
- Compression for large datasets

## Installation and Setup

1. **Prerequisites**:
   - Minecraft 1.20.4+
   - Fabric Loader 0.15.0+
   - Fabric API (latest)
   - Java 17+

2. **Installation**:
   ```bash
   # Download from releases or build from source
   gradle build
   # Copy to mods folder
   cp build/libs/umwa-mod-*.jar ~/.minecraft/mods/
   ```

3. **Configuration**:
   - Default keybindings: H (HUD), M (Minimap), U (Menu)
   - All features enabled by default
   - Performance auto-adjusts to hardware

## API Usage

### For Mod Developers

```java
// Get mining statistics
MiningDataManager manager = UMWAMod.getMiningDataManager();
int totalOres = manager.getCurrentSession().getTotalOres();

// Add custom waypoints
WaypointManager waypoints = UMWAMod.getWaypointManager();
waypoints.addWaypoint("My Mine", playerPos, Waypoint.WaypointType.CUSTOM);

// Get AI predictions
AIOrePrediction ai = UMWAMod.getAIOrePrediction();
double diamondChance = ai.predictOreChance(pos, Blocks.DIAMOND_ORE);
```

### Event Hooks

The mod provides several events for integration:
- `MiningDataEvent.ORE_FOUND`
- `CaveSystemEvent.DISCOVERED`
- `HotspotEvent.FOUND`
- `WaypointEvent.CREATED`

## Development Setup

### Building from Source

```bash
git clone https://github.com/Reattention/UMWA-Standalone-Minecraft-Mod.git
cd UMWA-Standalone-Minecraft-Mod
./gradlew build
```

### Testing

```bash
# Validate structure
./validate_structure.sh

# Run development environment
./gradlew runClient
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Implement changes with tests
4. Submit a pull request

## License

MIT License - see LICENSE file for details.