import React from "react";
import {
  LineChart,
  Line,
  ResponsiveContainer,
  Tooltip
} from "recharts";

const MiniChart = ({ data }) => {
  const chartData = data.map((snapshot) => ({
    price: snapshot.price,
    time: new Date(snapshot.timestamp).getTime()
  }));

  return (
    <ResponsiveContainer width={150} height={50}>
      <LineChart data={chartData}>
        <Line type="monotone" dataKey="price" stroke="#00b894" strokeWidth={2} dot={false} />
        <Tooltip content={null} />
      </LineChart>
    </ResponsiveContainer>
  );
};

export default MiniChart;
