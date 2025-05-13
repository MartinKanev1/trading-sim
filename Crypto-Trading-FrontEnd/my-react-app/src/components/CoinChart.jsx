import React from "react";
import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  Tooltip,
  ResponsiveContainer
} from "recharts";

const CoinChart = ({ snapshots }) => {
  console.log("✅ CoinChart component mounted");
  if (!snapshots || snapshots.length === 0) return <p>No chart data.</p>;

  const now = new Date();
  const oneDayAgo = new Date(now.getTime() - 24 * 60 * 60 * 1000);

  const filteredData = snapshots
    .map((snap) => ({
      time: new Date(snap.timestamp),
      price: snap.price
    }))
    .filter((point) => point.time >= oneDayAgo)
    .map((point) => ({
      ...point,
      label: point.time.toLocaleTimeString([], { hour: "2-digit", minute: "2-digit" })
    }));

  return (
    
    <div style={{ width: "100%", height: "100%", marginTop: 30 }}>
      <ResponsiveContainer width="100%" height="100%">
        <LineChart data={filteredData}>
          <XAxis dataKey="label" />
          <YAxis domain={["auto", "auto"]} />
          <Tooltip formatter={(value) => `$${value.toFixed(6)}`} />
          <Line
            type="monotone"
            dataKey="price"
            stroke="#3b82f6"
            strokeWidth={2}
            dot={false}
            isAnimationActive={true}
          />
        </LineChart>
      </ResponsiveContainer>
    </div>
  );
};

export default CoinChart;

