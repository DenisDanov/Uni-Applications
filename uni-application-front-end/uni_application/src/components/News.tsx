import React, {useState, useEffect} from "react";
import {NewsDTO} from "../types/NewsDTO";
import {fetchNews, addNews, deleteNews} from "../axios/requests";
import {
    Box,
    Button,
    Dialog,
    DialogActions,
    DialogContent,
    DialogTitle,
    Grid,
    TextField,
    Typography
} from "@mui/material";
import {AddCircleOutline, DeleteOutline} from "@mui/icons-material";
import {formatTime} from "../util/formatTime";
import {marked} from "marked";
import DOMPurify from 'dompurify';
import moment from "moment-timezone";
import SkeletonSequence from "./SkeletonSequence";
import {useKeycloak} from "../keycloak";

const News: React.FC = () => {
    const loggedUser = useKeycloak().keycloak;
    const [newsList, setNewsList] = useState<NewsDTO[]>([]);
    const [open, setOpen] = useState(false);
    const [newsData, setNewsData] = useState<Omit<NewsDTO, "id" | "authorUsername" | "creationTime">>({
        newsHeader: '',
        newsText: ''
    });
    const [errors, setErrors] = useState<{ newsHeader?: string, newsText?: string }>({});
    const isAdmin = loggedUser.hasRealmRole("admin") && loggedUser.hasRealmRole("FULL_ACCESS");
    const [minWidth, setMinWidth] = useState('35%'); // Default minWidth
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const loadNewsAndUpdateWidth = async () => {
            try {
                setLoading(true); // Start loading
                const news = await fetchNews();
                setNewsList(news);
                setLoading(false); // End loading
            } catch (error: any) {
                console.error(error.message);
                setLoading(false); // End loading in case of error
            }
        };

        loadNewsAndUpdateWidth();
    }, []);

    // This effect runs when `newsList` is updated
    useEffect(() => {
        const updateMinWidth = () => {
            const newsContainer = document.querySelector('.news-container');
            if (newsContainer) {
                // @ts-ignore
                const containerWidth = newsContainer.offsetWidth;
                setMinWidth(`${containerWidth}px`);
            }
        };

        if (newsList.length > 0) { // Ensure that newsList is not empty
            updateMinWidth();
            window.addEventListener('resize', updateMinWidth);

            // Cleanup listener on component unmount
            return () => window.removeEventListener('resize', updateMinWidth);
        }
    }, [newsList]); // Dependency array ensures this runs when `newsList` changes

    const handleOpen = () => setOpen(true);
    const handleClose = () => setOpen(false);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        setNewsData({...newsData, [e.target.name]: e.target.value});
    };

    const handleAddNews = async () => {
        // Basic validation
        const newErrors: { newsHeader?: string, newsText?: string } = {};
        if (!newsData.newsHeader) {
            newErrors.newsHeader = 'News Header is required';
        } else if (newsData.newsHeader.length > 1000) {
            newErrors.newsHeader = 'News Header cannot exceed 1000 characters';
        }
        if (!newsData.newsText) {
            newErrors.newsText = 'News Text is required';
        }
        if (Object.keys(newErrors).length > 0) {
            setErrors(newErrors);
            return;
        }

        try {
            const newNews: Omit<NewsDTO, "id"> = {
                ...newsData,
                authorUsername: loggedUser?.tokenParsed?.upn,
                creationTime: moment().tz('Europe/Sofia').format('YYYY-MM-DDTHH:mm:ss')
            };
            const addedNews = await addNews(newNews);
            setNewsList([...newsList, addedNews]);
            // @ts-ignore
            setErrors({});
            newsData.newsHeader = '';
            newsData.newsText = '';
            handleClose();
        } catch (error: any) {
            console.error(error.message);
        }
    };

    const handleDeleteNews = async (id: number) => {
        try {
            await deleteNews(id);
            setNewsList(newsList.filter(news => news.id !== id));
        } catch (error: any) {
            console.error(error.message);
        }
    };

    // Helper function to preprocess text for line breaks
    const preprocessText = (text: string) => {
        return text.replace(/\n\n{2,}/g, match => '<br>\n'.repeat(match.length - 1)).replace(/(?<!<br>)\n/g, '  \n').replace(/<br>/g, '\n<br>');
    };

    return (
        <Box>
            {isAdmin && (
                <Button
                    variant="contained"
                    color="primary"
                    startIcon={<AddCircleOutline/>}
                    onClick={handleOpen}
                    sx={{mb: 3}}
                >
                    Add News
                </Button>
            )}

            <Dialog
                open={open}
                onClose={handleClose}
                fullWidth
                maxWidth="md"
                sx={{
                    '.MuiDialog-container .MuiPaper-root': {
                        display: 'flex',
                        flexDirection: 'column',
                        height: 'fit-content',
                        maxWidth: "fit-content",
                        minWidth: minWidth,
                        padding: "0px",
                        marginLeft: "17px"
                    },
                }}
            >
                <DialogTitle>Add News</DialogTitle>
                <DialogContent>
                    <Box
                        sx={{
                            p: 2,
                            border: '1px solid #ddd',
                            borderRadius: 2,
                            boxShadow: 3,
                            flexGrow: 1,
                            display: 'flex',
                            flexDirection: 'column',
                        }}
                    >
                        <TextField
                            autoFocus
                            margin="dense"
                            name="newsHeader"
                            label="News Header"
                            type="text"
                            fullWidth
                            variant="outlined"
                            value={newsData.newsHeader}
                            onChange={handleChange}
                            error={!!errors.newsHeader}
                            helperText={errors.newsHeader}
                        />
                        <TextField
                            margin="dense"
                            name="newsText"
                            label="News Text"
                            type="text"
                            fullWidth
                            multiline
                            rows={4}
                            variant="outlined"
                            value={newsData.newsText}
                            onChange={handleChange}
                            error={!!errors.newsText}
                            helperText={errors.newsText}
                            sx={{
                                mt: 2,
                                flexGrow: 1,
                            }}
                            InputProps={{
                                inputProps: {
                                    style: {
                                        resize: 'both',
                                        overflow: 'auto',
                                    },
                                },
                            }}
                        />
                    </Box>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleClose} color="secondary">
                        Cancel
                    </Button>
                    <Button onClick={handleAddNews} color="primary">
                        Add
                    </Button>
                </DialogActions>
            </Dialog>

            <Typography textAlign={"center"} variant="h4" sx={{mb: 3}}>News</Typography>

            {loading ? (
                <SkeletonSequence/>
            ) : (
                <Grid id={"news-main-container"} container spacing={3}>
                    {newsList.map(news => (
                        <Grid item xs={12} md={6} lg={4} key={news.id}>
                            <Box className={"news-container"} sx={{
                                p: 2,
                                border: '1px solid #ddd',
                                borderRadius: 2,
                                boxShadow: 3,
                                position: 'relative'
                            }}>
                                <Typography
                                    // @ts-ignore
                                    dangerouslySetInnerHTML={{__html: DOMPurify.sanitize(marked(preprocessText(news.newsHeader)))}}
                                />
                                <Typography
                                    style={{
                                        wordWrap: 'break-word',
                                        maxWidth: '100%',
                                        overflow: 'auto',
                                        boxSizing: 'border-box',
                                    }}
                                    variant="body2"
                                    sx={{mt: 1, mb: 2}}
                                    component="div"
                                    // @ts-ignore
                                    dangerouslySetInnerHTML={{__html: DOMPurify.sanitize(marked(preprocessText(news.newsText)))}}
                                    className="markdown-content"
                                />
                                <Typography variant="caption" color="textSecondary">
                                    By: {news.authorUsername} | {formatTime(news.creationTime)}
                                </Typography>
                                {isAdmin && (
                                    <Button
                                        id={"delete-news-btn"}
                                        variant="outlined"
                                        color="error"
                                        startIcon={<DeleteOutline/>}
                                        sx={{mt: 2, position: 'absolute', right: 8, bottom: 8}}
                                        onClick={() => handleDeleteNews(news.id!)}
                                    >
                                        Delete
                                    </Button>
                                )}
                            </Box>
                        </Grid>
                    ))}
                </Grid>
            )}
        </Box>
    );
};

export default News;
