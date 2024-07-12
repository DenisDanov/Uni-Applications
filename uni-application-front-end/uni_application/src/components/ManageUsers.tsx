import React, {useEffect, useState} from "react";
import {
    Alert,
    Button,
    Card,
    CardActions,
    CardContent,
    CircularProgress,
    Container,
    FormControl,
    Grid,
    InputLabel,
    MenuItem,
    Select,
    Typography,
} from "@mui/material";
import {useKeycloak} from "../keycloak";
import {
    fetchAccessLevels,
    fetchFilteredUsers,
    fetchRoles,
    fetchUsers,
    toggleBlockStatus,
    updateUserRoles
} from "../axios/requests";
import Filter from "./Filter";

const deepCopy = (obj: any) => JSON.parse(JSON.stringify(obj));

const ManageUsers: React.FC = () => {
    const [users, setUsers] = useState<any[]>([]);
    const [tempUsers, setTempUsers] = useState<any[]>([]);
    const [roles, setRoles] = useState<any[]>([]);
    const [accessLevels, setAccessLevels] = useState<any[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [success, setSuccess] = useState<string | null>(null);
    const {keycloak} = useKeycloak();

    const filterConfig = [
        {name: 'username', type: 'text'},
        {name: 'email', type: 'text'},
        {name: 'phoneNumber', type: 'text'},
        {name: 'maxResults', type: 'number'},
    ];

    useEffect(() => {
        const fetchData = async () => {
            try {
                const usersData = await fetchUsers();
                setUsers(usersData);
                setTempUsers(deepCopy(usersData));

                const rolesData = await fetchRoles();
                setRoles(rolesData);

                const accessLevelsData = await fetchAccessLevels();
                setAccessLevels(accessLevelsData);

                setLoading(false);
            } catch (err: any) {
                setError(err.message);
                setLoading(false);
            }
        };

        fetchData();
    }, []);

    const handleSearch = (activeFilters: any) => {
        fetchFilteredUsers(activeFilters)
            .then((users) => {
                setTempUsers(users);
                setUsers(users);
            })
            .catch((err) => {
                setError(err.message);
            });
    };

    const handleRoleChange = (index: number, newRole: string) => {
        const updatedTempUsers = [...tempUsers];
        const updatedUsers = [...users];
        updatedTempUsers[index].roleDTO.role = newRole;
        updatedUsers[index].roleDTO.role = newRole;
        if (newRole === "STUDENT") {
            updatedTempUsers[index].accessLevel = undefined;
        } else if (newRole === "ADMIN") {
            updatedTempUsers[index].accessLevel = accessLevels[1];
        }
        setTempUsers(updatedTempUsers);
    };

    const handleAccessLevelChange = (index: number, newAccessLevel: string) => {
        const updatedTempUsers = [...tempUsers];
        let updatedUser = updatedTempUsers[index];
        updatedUser.accessLevel = {
            accessType: newAccessLevel,
            accessDescription: `${
                newAccessLevel === "FULL_ACCESS"
                    ? "Full access to all resources"
                    : "Limited access to read only"
            }`,
        };
        setTempUsers(updatedTempUsers);
    };

    const handleUpdate = async (index: number) => {
        try {
            const user = tempUsers[index];
            const updatedUser = await updateUserRoles(user);

            const updatedUsers = [...users];
            updatedUsers[index] = updatedUser;
            setUsers(updatedUsers);

            const updatedTempUsers = [...tempUsers];
            updatedTempUsers[index] = updatedUser;
            setTempUsers(deepCopy(updatedTempUsers));

            setSuccess("Successfully updated user.");
            setTimeout(() => {
                setSuccess(null);
            }, 5000);
        } catch (err: any) {
            setError(err.message);
            setTimeout(() => {
                setError(null);
            }, 5000);
        }
    };

    const handleBlockStatus = async (index: number) => {
        try {
            const user = users[index];
            const enabled = user.enabled;

            await toggleBlockStatus(user.username, enabled);

            const updatedUsers = [...users];
            updatedUsers[index].enabled = !enabled;
            setUsers(updatedUsers);

            const updatedTempUsers = [...tempUsers];
            updatedTempUsers[index].enabled = !enabled;
            setTempUsers(deepCopy(updatedTempUsers));

            setSuccess(`Successfully ${enabled ? "unblock" : "block"}ed user.`);
            setTimeout(() => {
                setSuccess(null);
            }, 5000);
        } catch (err: any) {
            setError(err.message);
            setTimeout(() => {
                setError(null);
            }, 5000);
        }
    };

    if (loading) {
        return (
            <Container>
                <CircularProgress/>
            </Container>
        );
    }

    return (
        <Container>
            {error &&
                <Container style={{paddingBottom: "10px"}}>
                    <Alert severity="error">{error}</Alert>
                </Container>
            }
            {success &&
                <Container style={{paddingBottom: "10px"}}>
                    <Alert severity="success">{success}</Alert>
                </Container>
            }
            <Filter
                // @ts-ignore
                filters={filterConfig}
                onSearch={handleSearch}
                filterName={'Filter Users'}/>
            <Grid container spacing={3}>
                {tempUsers.map((user, index) => (
                    <Grid item xs={12} md={6} lg={4} key={index}>
                        <Card>
                            <CardContent>
                                <Typography variant="h6">{user.username}</Typography>
                                <Typography variant="body2">Email: {user.email}</Typography>
                                <Typography variant="body2">First Name: {user.firstName}</Typography>
                                <Typography variant="body2">Middle Name: {user.middleName}</Typography>
                                <Typography variant="body2">Last Name: {user.lastName}</Typography>
                                {user.dateOfBirth && (
                                    <Typography variant="body2">Date of Birth: {user.dateOfBirth}</Typography>
                                )}
                                {user.phoneNumber && (
                                    <Typography variant="body2">Phone Number: {user.phoneNumber}</Typography>
                                )}
                                <FormControl fullWidth margin="normal">
                                    <InputLabel>Role</InputLabel>
                                    <Select
                                        value={user.roleDTO.role}
                                        onChange={(e) => handleRoleChange(index, e.target.value as string)}
                                        disabled={
                                            !keycloak.hasRealmRole("FULL_ACCESS") ||
                                            (users[index].roleDTO.role === "ADMIN" && users[index].accessLevel?.accessType === "FULL_ACCESS")
                                        }
                                        label={"Role"}
                                    >
                                        {roles.map((role) => (
                                            <MenuItem key={role.role} value={role.role}>
                                                {role.roleDescription}
                                            </MenuItem>
                                        ))}
                                    </Select>
                                </FormControl>
                                <FormControl fullWidth margin="normal">
                                    <InputLabel>Access Level</InputLabel>
                                    <Select
                                        value={user.accessLevel?.accessType || ""}
                                        onChange={(e) => handleAccessLevelChange(index, e.target.value as string)}
                                        disabled={
                                            !keycloak.hasRealmRole("FULL_ACCESS") ||
                                            users[index].roleDTO.role === "STUDENT" ||
                                            (users[index].roleDTO.role === "ADMIN" && users[index].accessLevel?.accessType === "FULL_ACCESS")
                                        }
                                        label={"Access Level"}
                                    >
                                        {accessLevels.map((accessLevel) => (
                                            <MenuItem key={accessLevel.accessType} value={accessLevel.accessType}>
                                                {accessLevel.accessDescription}
                                            </MenuItem>
                                        ))}
                                    </Select>
                                </FormControl>
                            </CardContent>
                            <CardActions>
                                <Button
                                    size="small"
                                    color="primary"
                                    onClick={() => handleUpdate(index)}
                                    disabled={
                                        !keycloak.hasRealmRole("FULL_ACCESS") ||
                                        (users[index].roleDTO.role === "ADMIN" && users[index].accessLevel?.accessType === "FULL_ACCESS")
                                    }
                                >
                                    Update
                                </Button>
                                {user.enabled ? (
                                    <Button
                                        size="small"
                                        color="error"
                                        onClick={() => handleBlockStatus(index)}
                                        disabled={
                                            !keycloak.hasRealmRole("FULL_ACCESS") ||
                                            (users[index].roleDTO.role === "ADMIN" && users[index].accessLevel?.accessType === "FULL_ACCESS")
                                        }
                                    >
                                        Block User
                                    </Button>
                                ) : (
                                    <Button
                                        size="small"
                                        color="error"
                                        onClick={() => handleBlockStatus(index)}
                                        disabled={!keycloak.hasRealmRole("FULL_ACCESS") ||
                                            (users[index].roleDTO.role === "ADMIN" && users[index].accessLevel?.accessType === "FULL_ACCESS")}
                                    >
                                        Unblock User
                                    </Button>
                                )}
                            </CardActions>
                        </Card>
                    </Grid>
                ))}
            </Grid>
        </Container>
    );
};

export default ManageUsers;
