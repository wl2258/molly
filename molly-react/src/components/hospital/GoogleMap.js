import React, { useCallback, useEffect, useRef } from "react";

function GoogleMap() {
  const mapRef = useRef(null);

  const initMap = useCallback(() => {
    new window.google.maps.Map(mapRef.current, {
      center: { lat: 36.650879, lng: 127.468983 },
      zoom: 8,
    });
  }, [mapRef]);

  useEffect(() => {
    initMap();
  }, [initMap]);

  return (
    <div
      style={{ width: "700px", height: "400px" }}
      ref={mapRef}
    ></div>
  );
}

export default GoogleMap;