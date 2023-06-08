import React, { useEffect, useState } from "react";
import {
  GoogleMap,
  InfoWindow,
  Marker,
  useLoadScript,
} from "@react-google-maps/api";
import styles from "../../css/HospitalMap.module.css";
import {
  MdOutlineStarOutline,
  MdOutlineStarHalf,
  MdOutlineStar,
  MdSearch,
} from "react-icons/md";

const libraries = ["places"];
const mapContainerStyle = {
  width: "65%",
  height: "400px",
};

const myStyles = [
  {
    featureType: "poi",
    elementType: "labels",
    stylers: [{ visibility: "off" }],
  },
];

const options = {
  disableDefaultUI: true,
  styles: myStyles,
};

const HospitalMap = () => {
  const [map, setMap] = useState(null);
  const [searchResults, setSearchResults] = useState([]);
  const [searchQuery, setSearchQuery] = useState("");
  const [currentLocation, setCurrentLocation] = useState(null);
  const [search, setSearch] = useState(false);
  const [selectedMarker, setSelectedMarker] = useState(null);
  let count = 0;

  const { isLoaded, loadError } = useLoadScript({
    googleMapsApiKey: "AIzaSyCvNXc4xAKwBK99wTMBapkko0Oqs4Kdqro",
    libraries,
  });

  useEffect(() => {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(
        (position) => {
          const { latitude, longitude } = position.coords;
          const location = { lat: latitude, lng: longitude };
          setCurrentLocation(location);

          if (map !== null) {
            const service = new window.google.maps.places.PlacesService(map);

            const request = {
              location,
              radius: "10000",
              query: "동물병원",
            };

            service.textSearch(request, (results, status) => {
              if (status === window.google.maps.places.PlacesServiceStatus.OK) {
                const sortedResults = results.sort((a, b) => {
                  const aOpenNow = a.opening_hours && a.opening_hours.open_now;
                  const bOpenNow = b.opening_hours && b.opening_hours.open_now;

                  if (aOpenNow && !bOpenNow) {
                    return -1; // a가 영업 중이고 b가 영업 종료인 경우 a를 앞으로
                  } else if (!aOpenNow && bOpenNow) {
                    return 1; // b가 영업 중이고 a가 영업 종료인 경우 b를 앞으로
                  }
                  return 0; // 영업 여부가 같거나 정보가 없는 경우 순서 변경 없음
                });

                setSearchResults(sortedResults);
              }
            });
          }
        },
        (error) => {
          console.error("Error getting the user's location:", error);
        }
      );
    } else {
      console.error("Geolocation is not supported by this browser.");
    }
  }, [map]);

  const onMapLoad = (map) => {
    setMap(map);
  };

  const onPlacesChanged = () => {
    if (map && searchQuery !== "") {
      const service = new window.google.maps.places.PlacesService(map);

      const request = {
        location: map.getCenter(),
        radius: "10000",
        query: `${searchQuery} 동물병원`,
      };

      service.textSearch(request, (results, status) => {
        if (status === window.google.maps.places.PlacesServiceStatus.OK) {
          const sortedResults = results.sort((a, b) => {
            const aOpenNow = a.opening_hours && a.opening_hours.open_now;
            const bOpenNow = b.opening_hours && b.opening_hours.open_now;

            if (aOpenNow && !bOpenNow) {
              return -1; // a가 영업 중이고 b가 영업 종료인 경우 a를 앞으로
            } else if (!aOpenNow && bOpenNow) {
              return 1; // b가 영업 중이고 a가 영업 종료인 경우 b를 앞으로
            }
            return 0; // 영업 여부가 같거나 정보가 없는 경우 순서 변경 없음
          });

          setSearchResults(sortedResults);
          setSearch(true);
        }
      });
    }
  };

  const handleMarkerClick = (marker) => {
    setSelectedMarker(marker);
  };

  return (
    <div style={{ marginTop: "-20px" }}>
      <div className={styles.search}>
        <input
          placeholder="지역을 입력하세요"
          type="text"
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
          onKeyDown={(e) => {
            if (e.key === "Enter") {
              onPlacesChanged();
            }
          }}
        />
        <span onClick={onPlacesChanged}>
          <MdSearch color="#AFA79F" />
        </span>
      </div>
      <div className={styles.map}>
        {isLoaded ? (
          <GoogleMap
            mapContainerStyle={mapContainerStyle}
            center={
              !search ? currentLocation : searchResults[0].geometry.location
            }
            zoom={13}
            options={options}
            onLoad={onMapLoad}
          >
            <Marker
              position={currentLocation}
              icon={{
                url: process.env.PUBLIC_URL + "/img/marker.png",
                scaledSize: new window.google.maps.Size(26, 32),
              }}
            ></Marker>
            {searchResults.map((result) => (
              <>
                <Marker
                  key={result.place_id}
                  position={result.geometry.location}
                  icon={{
                    url: process.env.PUBLIC_URL + "/img/hospital-marker.png",
                    scaledSize: new window.google.maps.Size(26, 32),
                  }}
                  onClick={() => handleMarkerClick(result)}
                />
                {selectedMarker && (
                  <InfoWindow
                    position={selectedMarker.geometry.location}
                    onCloseClick={() => setSelectedMarker(null)}
                    options={{
                      pixelOffset: new window.google.maps.Size(0, -25),
                    }}
                  >
                    <div className={styles.infoWindow}>
                      {console.log(selectedMarker)}
                      <p>{selectedMarker.name}</p>
                    </div>
                  </InfoWindow>
                )}
              </>
            ))}
          </GoogleMap>
        ) : (
          <div>Loading...</div>
        )}
        <div className={styles.night}>
          <h3>야간 동물 병원 🌙</h3>
          {searchResults.map((result, index) => {
            if (result.name.includes("24시")) {
              count++;
              return (
                <div>
                  <p>{result.name}</p>
                </div>
              );
            } else if (count === 0 && index === searchResults.length - 1) {
              return (
                <div>
                  <p>근처에 야간 동물병원이 없습니다.</p>
                </div>
              );
            }
          })}
        </div>
      </div>
      <div>
        {searchResults.map((result) => (
          <div key={result.place_id} className={styles.hospitalList}>
            <div>
              <h3>{result.name}</h3>
              <p>{result.formatted_address}</p>
            </div>
            <div>
              <p>{result.rating}</p>
              <div>
                {result.rating === 0 ? (
                  Array(5)
                    .fill()
                    .map(() => {
                      return (
                        <MdOutlineStarOutline size="18px" color="#EBC351" />
                      );
                    })
                ) : result.rating > 0 && result.rating < 1 ? (
                  <>
                    <MdOutlineStarHalf size="18px" color="#EBC351" />{" "}
                    {Array(4)
                      .fill()
                      .map(() => {
                        return (
                          <MdOutlineStarOutline size="18px" color="#EBC351" />
                        );
                      })}
                  </>
                ) : result.rating === 1 ? (
                  <>
                    <MdOutlineStar size="18px" color="#EBC351" />{" "}
                    {Array(4)
                      .fill()
                      .map(() => {
                        return (
                          <MdOutlineStarOutline size="18px" color="#EBC351" />
                        );
                      })}
                  </>
                ) : result.rating > 1 && result.rating < 2 ? (
                  <>
                    <MdOutlineStar size="18px" color="#EBC351" />{" "}
                    <MdOutlineStarHalf size="18px" color="#EBC351" />{" "}
                    {Array(3)
                      .fill()
                      .map(() => {
                        return (
                          <MdOutlineStarOutline size="18px" color="#EBC351" />
                        );
                      })}
                  </>
                ) : result.rating === 2 ? (
                  <>
                    {Array(2)
                      .fill()
                      .map(() => {
                        return <MdOutlineStar size="18px" color="#EBC351" />;
                      })}{" "}
                    {Array(3)
                      .fill()
                      .map(() => {
                        return (
                          <MdOutlineStarOutline size="18px" color="#EBC351" />
                        );
                      })}
                  </>
                ) : result.rating > 2 && result.rating < 3 ? (
                  <>
                    {Array(2)
                      .fill()
                      .map(() => {
                        return <MdOutlineStar size="18px" color="#EBC351" />;
                      })}{" "}
                    <MdOutlineStarHalf size="18px" color="#EBC351" />{" "}
                    {Array(2)
                      .fill()
                      .map(() => {
                        return (
                          <MdOutlineStarOutline size="18px" color="#EBC351" />
                        );
                      })}
                  </>
                ) : result.rating === 3 ? (
                  <>
                    {Array(3)
                      .fill()
                      .map(() => {
                        return <MdOutlineStar size="18px" color="#EBC351" />;
                      })}{" "}
                    {Array(2)
                      .fill()
                      .map(() => {
                        return (
                          <MdOutlineStarOutline size="18px" color="#EBC351" />
                        );
                      })}
                  </>
                ) : result.rating > 3 && result.rating < 4 ? (
                  <>
                    {Array(3)
                      .fill()
                      .map(() => {
                        return <MdOutlineStar size="18px" color="#EBC351" />;
                      })}{" "}
                    <MdOutlineStarHalf size="18px" color="#EBC351" />{" "}
                    {Array(1)
                      .fill()
                      .map(() => {
                        return (
                          <MdOutlineStarOutline size="18px" color="#EBC351" />
                        );
                      })}
                  </>
                ) : result.rating === 4 ? (
                  <>
                    {Array(4)
                      .fill()
                      .map(() => {
                        return <MdOutlineStar size="18px" color="#EBC351" />;
                      })}{" "}
                    {Array(1)
                      .fill()
                      .map(() => {
                        return (
                          <MdOutlineStarOutline size="18px" color="#EBC351" />
                        );
                      })}
                  </>
                ) : result.rating > 4 && result.rating < 5 ? (
                  <>
                    {Array(4)
                      .fill()
                      .map(() => {
                        return <MdOutlineStar size="18px" color="#EBC351" />;
                      })}{" "}
                    <MdOutlineStarHalf size="18px" color="#EBC351" />
                  </>
                ) : (
                  Array(5)
                    .fill()
                    .map(() => {
                      return <MdOutlineStar size="18px" color="#EBC351" />;
                    })
                )}
              </div>
              <p>{`(${result.user_ratings_total})`}</p>
            </div>
            <div className={styles.open}>
              {result.opening_hours &&
              result.opening_hours.open_now === true ? (
                <h4>영업 중</h4>
              ) : result.opening_hours &&
                result.opening_hours.open_now === false ? (
                <h4>영업 종료</h4>
              ) : null}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default HospitalMap;
