/*import { MapContainer, TileLayer, Marker, Popup } from "react-leaflet";
import { useMap } from "react-leaflet/hooks";
import "../dist/leaflet.css";

export const ChangeView = ({ center }) => {
  const map = useMap();
  map.flyTo(center, map.getZoom());
  return null;
};

export const createIcon = (url) => {
  return new L.Icon({
    iconUrl: url,
    iconAnchor: [30, 70],
  });
};

const MapLeaflet = ({ center, zoom, region, position }) => {
  const markerPosition = position;
  return (
    <MapContainer
      center={center}
      zoom={zoom}
      scrollWheelZoom={false}
      className="h-[calc(100vh_-_280px)] z-30"
    >
      <ChangeView center={center} zoom={zoom} />
      <TileLayer
        attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
      />
      <Marker position={markerPosition} icon={createIcon("/icon-location.svg")}>
        <Popup>Hello there, This is {region}</Popup>
      </Marker>
    </MapContainer>
  );
};

export default MapLeaflet;*/
