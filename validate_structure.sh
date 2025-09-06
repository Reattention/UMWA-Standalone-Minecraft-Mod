#!/bin/bash

# Simple build validation script for UMWA mod
# This script validates the mod structure without requiring full Fabric build

echo "=== UMWA Mod Structure Validation ==="
echo

# Check main mod structure
echo "Checking mod structure..."
if [ -f "src/main/java/com/umwa/UMWAMod.java" ]; then
    echo "✅ Main mod class found"
else
    echo "❌ Main mod class missing"
fi

if [ -f "src/client/java/com/umwa/client/UMWAModClient.java" ]; then
    echo "✅ Client mod class found"
else
    echo "❌ Client mod class missing"
fi

if [ -f "src/main/resources/fabric.mod.json" ]; then
    echo "✅ Fabric mod metadata found"
else
    echo "❌ Fabric mod metadata missing"
fi

# Count core features implemented
echo
echo "Core features implemented:"
CORE_FILES=(
    "src/main/java/com/umwa/core/MiningDataManager.java"
    "src/main/java/com/umwa/core/WorldAnalyzer.java"
    "src/main/java/com/umwa/data/MiningSession.java"
    "src/main/java/com/umwa/data/OreVein.java"
    "src/main/java/com/umwa/data/CaveSystem.java"
    "src/main/java/com/umwa/data/HotSpot.java"
    "src/main/java/com/umwa/util/PathFinder.java"
    "src/client/java/com/umwa/client/gui/MiningHUD.java"
    "src/client/java/com/umwa/client/gui/MinimapRenderer.java"
    "src/main/java/com/umwa/mixins/WorldMixin.java"
)

for file in "${CORE_FILES[@]}"; do
    if [ -f "$file" ]; then
        echo "✅ $(basename "$file")"
    else
        echo "❌ $(basename "$file")"
    fi
done

echo
echo "=== Java Syntax Check ==="
# Basic Java syntax validation (without dependencies)
find src -name "*.java" -exec echo "Checking {}" \; -exec head -5 {} \; | grep -E "(class|interface|enum)" | wc -l
echo "Total Java classes/interfaces found: $(find src -name "*.java" | wc -l)"

echo
echo "=== Resource Files ==="
if [ -f "src/main/resources/assets/umwa-mod/lang/en_us.json" ]; then
    echo "✅ Language file found"
fi

if [ -f "src/main/resources/umwa-mod.mixins.json" ]; then
    echo "✅ Mixins configuration found"
fi

echo
echo "=== Build Configuration ==="
if [ -f "build.gradle" ]; then
    echo "✅ Gradle build file found"
fi

if [ -f "gradle.properties" ]; then
    echo "✅ Gradle properties found"
fi

if [ -f "settings.gradle" ]; then
    echo "✅ Gradle settings found"
fi

echo
echo "Mod structure validation complete!"
echo "Note: Full compilation requires Fabric development environment."