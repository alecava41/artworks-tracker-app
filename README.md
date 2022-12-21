# Artworks Tracker - Android App
## Accessibility
- Audio description of the artworks (integrated -> icon to toggle);
  - How to implement it?
- General [Best Practices Jetpack Compose](https://developer.android.com/jetpack/compose/accessibility);
- Suggested path for wheelchairs:
  - Drawn in the map
- [Other useful things](https://developer.android.com/guide/topics/ui/accessibility)

## Features
### Settings
  - Language selection (?)
  - Accessibility
    - Night Mode
    - ...

### Artwork "view"
- Presentation (presentation): 
  - UI (compose) (column order): title, author, (audio description toggle), pictures (slideshow), description
  - VM
  - TextToSpeech (?)
- Model (entities) (data): 
  - Artwork
  - Data source (repository) (data):
    - Remote server (Retrofit) (fetch artwork data from the remote db)
    - Room database (cache of fetched data)
  - Use Cases (domain):
    - GetArtworkUseCase (given an ID get the artwork's data)

### Museum map
- Presentation (presentation):
  - UI (compose): Map:
    - Room's perimeter
    - Artworks' position
    - Clickable icon 
    - Already seen: should fire "artwork view" feature (show the artwork data)
    - Not seen yet: should provide a **SPECIFIC** path to reach it (?)
    - **GENERAL** path to navigate the museum
    - User position (point, ...)
  - VM
    - Should observe the beacon flow of the UseCase
    - Should modify the uiState every time a new value "arrives" from the observed flow
    - UiState = data_class (beaconId, alreadySeen)
      - alreadySeen
        - If "beacon" not seen yet -> automatic "navigation"
        - If "beacon" already seen -> snackbar
    - beaconVisited = ConcurrentLinkedQueue<UUID>()
  - Model (data):
    - Beacon (id of the beacon)
  - Data source (repository) (data):
    - Interface (should be able to periodically provide info about close beacons) 
    - AltBeacon library (somehow)
      - Should expose only a Flow<List<Beacon>> that emits a value periodically ("didRangeBeaconsInRegion" override)
  - Use Cases (domain):
    - GetCloserBeaconsUseCase
      - (Other business logic to determine when you are closer to a beacon)
      - beaconsMap = ConcurrentHashMap<UUID, BeaconMeasurementContainer>()
      - Should expose only a Flow<Beacon> that emits a value every time the user is really close to the beacon
    
  