import React from "react";
import {Grid, Skeleton} from "@mui/material";

const SkeletonSequence: React.FC = () => {
    return (
        <Grid container spacing={3}>
            {Array.from({length: 3}).map((_, index) => (
                <Grid item xs={12} md={6} lg={4} key={index}>
                    <Skeleton
                        animation="wave"
                        variant="rounded"
                        height={200}
                        sx={{position: "relative", zIndex: 2}}
                    />
                    <Skeleton
                        variant="rounded"
                        animation="wave"
                        sx={{mt: 2, width: "80%"}}
                    />
                    <Skeleton
                        variant="rounded"
                        animation="wave"
                        sx={{mt: 1, width: "60%"}}
                    />
                    <Skeleton
                        variant="rounded"
                        animation="wave"
                        sx={{mt: 1, width: "40%"}}
                    />
                </Grid>
            ))}
        </Grid>
    );
};

export default SkeletonSequence;
