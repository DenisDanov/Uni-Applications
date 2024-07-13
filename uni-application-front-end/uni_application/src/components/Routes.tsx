import React from 'react';
import {Navigate} from 'react-router-dom';
import SecurityGuard from '../security/SecurityGuard';
import {useKeycloak} from '../keycloak';
import Specialties from "./Specialties";
import Faculties from "./Faculties";
import Application from "./Application";
import Home from "./Home";
import Profile from "./Profile";
import ManageStudentApplications from "./ManageStudentApplications";
import ManageUsers from "./ManageUsers";
import EvaluateApplication from "./EvaluateApplication";
import RequirementsTests from "./RequirementsTests";

const HomeRoute: React.FC = () => <Home/>;
const ProfileRoute: React.FC = () => <Profile/>;
const FacultiesRoute: React.FC = () => <Faculties/>;
const SpecialtiesRoute: React.FC = () => <Specialties/>;
const Apply: React.FC = () => <Application/>
const ManageStudentApplicationsRoute: React.FC = () => <ManageStudentApplications/>;
const ManageUsersRoute: React.FC = () => <ManageUsers/>;
const EvaluateApplicationRoute: React.FC = () => <EvaluateApplication/>
const RequirementsTestsRoute: React.FC = () => <RequirementsTests/>

const Login: React.FC = () => {
    const {keycloak} = useKeycloak();
    React.useEffect(() => {
        keycloak.login();
    }, [keycloak]);
    return <Navigate to="/"/>;
};

const routes = [
    {
        path: "/",
        element: <HomeRoute/>,
    },
    {
        path: "/faculties",
        element: <FacultiesRoute/>,
    },
    {
        path: "/specialties",
        element: <SpecialtiesRoute/>,
    },
    {
        path: "/login",
        element: <Login/>,
    },
    {
        path: "/profile",
        element: (
            <SecurityGuard
                doCheck={true}
                checkForRoles={["admin", "student"]}
                roleOperator="or"
                loginOnUnauthorized={false}
                displayOnUnauthorized={<Navigate to={"/"}/>}
            >
                <ProfileRoute/>
            </SecurityGuard>
        ),
    },
    {
        path: "/apply",
        element: (
            <SecurityGuard
                doCheck={true}
                checkForRoles={['student']}
                loginOnUnauthorized={false}
                displayOnUnauthorized={<Navigate to={"/login"}/>}
            >
                <Apply/>
            </SecurityGuard>
        ),
    },
    {
        path: "/manage-applications",
        element: (
            <SecurityGuard
                doCheck={true}
                checkForRoles={['admin']}
                loginOnUnauthorized={false}
                displayOnUnauthorized={<Navigate to="/"/>}
            >
                <ManageStudentApplicationsRoute/>
            </SecurityGuard>
        )
    },
    {
        path: "/manage-users",
        element: (
            <SecurityGuard
                doCheck={true}
                checkForRoles={['admin']}
                loginOnUnauthorized={false}
                displayOnUnauthorized={<Navigate to="/"/>}
            >
                <ManageUsersRoute/>
            </SecurityGuard>
        )
    },
    {
        path: "/evaluate-application",
        element: (
            <SecurityGuard
                doCheck={true}
                checkForRoles={['admin']}
                loginOnUnauthorized={false}
                displayOnUnauthorized={<Navigate to="/"/>}
            >
                <EvaluateApplicationRoute/>
            </SecurityGuard>
        )
    },
    {
        path: "/requirements-tests",
        element: (
            <SecurityGuard
                doCheck={true}
                checkForRoles={['student']}
                loginOnUnauthorized={false}
                displayOnUnauthorized={<Navigate to="/"/>}
            >
                <RequirementsTestsRoute/>
            </SecurityGuard>
        )
    }
];

export default routes;
