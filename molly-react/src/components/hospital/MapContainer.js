import React, { useEffect, useRef } from "react";

const { kakao } = window;

const MapContainer = () => {
  const mapRef = useRef(null); // 카카오맵을 표시할 div 요소의 참조

  useEffect(() => {
    const script = document.createElement("script");
    script.src =
      "https://dapi.kakao.com/v2/maps/sdk.js?appkey=d5fc8971cb70689d4cfa27ebb719f27b&autoload=false";
    // YOUR_API_KEY에는 본인의 카카오맵 API 키를 입력해야 합니다.
    document.head.appendChild(script);

    script.onload = () => {
      // 카카오맵 API 스크립트가 로드된 후 실행됩니다.
      kakao.maps.load(() => {
        const container = mapRef.current;
        const options = {
          center: new kakao.maps.LatLng(37.5665, 126.978), // 초기 맵 중심 좌표 (서울시)
          level: 5, // 초기 확대/축소 레벨
        };
        const map = new kakao.maps.Map(container, options);

        // 현재 위치를 가져와서 중심 좌표로 설정합니다.
        navigator.geolocation.getCurrentPosition(
          (position) => {
            const { latitude, longitude } = position.coords;
            const latLng = new kakao.maps.LatLng(latitude, longitude);
            map.setCenter(latLng);

            // 주변 동물병원 마커를 표시하는 함수
            const displayNearbyAnimalHospitals = () => {
              const places = new kakao.maps.services.Places();
              places.keywordSearch("동물병원", (results, status) => {
                if (status === kakao.maps.services.Status.OK) {
                  // 검색 결과를 받아와서 마커로 표시합니다.
                  for (let i = 0; i < results.length; i++) {
                    const marker = new kakao.maps.Marker({
                      position: new kakao.maps.LatLng(
                        results[i].y,
                        results[i].x
                      ),
                      map: map,
                    });

                    marker.setMap(map);
                  }
                }
              });
            };

            displayNearbyAnimalHospitals(); // 주변 동물병원 마커를 표시합니다.
          },
          (error) => {
            console.error("Error getting current position:", error);
          }
        );
      });
    };

    return () => {
      // 컴포넌트 언마운트 시 카카오맵 API 스크립트를 제거합니다.
      document.head.removeChild(script);
    };
  }, []);

  return <div style={{ width: "100%", height: "400px" }} ref={mapRef}></div>;
};

export default MapContainer;
